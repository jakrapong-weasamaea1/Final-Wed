document.addEventListener("DOMContentLoaded", function () {
    const totalComputers = 15;
    let selectedPcId = null;

    const bookings = [
        { id: "COM-02", name: "Ahmad", idCode: "6612345", slot: "09:00 - 11:00" },
        { id: "COM-08", name: "Fatimah", idCode: "6654321", slot: "13:00 - 15:00" }
    ];

    const computerGrid = document.getElementById("computerGrid");
    const labBookingForm = document.getElementById("labBookingForm");
    const bookingListBody = document.getElementById("bookingListBody");
    const navMenu = document.getElementById("navMenu");
    const burgerBtn = document.getElementById("burgerBtn");
    const selectedPcInput = document.getElementById("selectedPc");
    const studentNameInput = document.getElementById("studentName");

    const currentUserData = localStorage.getItem("currentUser");
    const currentUser = currentUserData ? JSON.parse(currentUserData) : null;

    if (currentUser && currentUser.isLoggedIn) {
        if (studentNameInput) {
            studentNameInput.value = currentUser.username;
        }

        if (navMenu) {
            const existingLogoutBtn = document.getElementById("logoutBtn");
            if (!existingLogoutBtn) {
                const logoutBtn = document.createElement("a");
                logoutBtn.href = "#";
                logoutBtn.id = "logoutBtn";
                logoutBtn.style.color = "#e74c3c";
                logoutBtn.textContent = `👤 ${currentUser.username} (Logout)`;
                navMenu.appendChild(logoutBtn);
            }

            const logoutBtn = document.getElementById("logoutBtn");
            if (logoutBtn) {
                logoutBtn.addEventListener("click", function (event) {
                    event.preventDefault();
                    if (confirm("คุณต้องการออกจากระบบใช่หรือไม่?")) {
                        localStorage.removeItem("currentUser");
                        window.location.href = "login.html";
                    }
                });
            }
        }
    } else if (navMenu) {
        const loginLink = navMenu.querySelector('a[href="login.html"]');
        if (!loginLink) {
            const loginNav = document.createElement("a");
            loginNav.href = "login.html";
            loginNav.textContent = "🔑 เข้าสู่ระบบ (Login)";
            navMenu.appendChild(loginNav);
        }
    }

    function renderComputers() {
        if (!computerGrid) return;
        computerGrid.innerHTML = "";

        for (let i = 1; i <= totalComputers; i++) {
            const pcId = `COM-${i < 10 ? "0" + i : i}`;
            const isBooked = bookings.some((booking) => booking.id === pcId);
            const isSelected = selectedPcId === pcId;

            const card = document.createElement("div");
            card.className = `computer-card ${isBooked ? "booked" : isSelected ? "selected" : "available"}`;
            card.textContent = pcId;

            if (!isBooked) {
                card.addEventListener("click", function () {
                    selectComputer(pcId);
                });
            }

            computerGrid.appendChild(card);
        }
    }

    function selectComputer(pcId) {
        selectedPcId = pcId;
        if (selectedPcInput) {
            selectedPcInput.value = pcId;
        }
        renderComputers();
    }

    function renderBookingsTable() {
        if (!bookingListBody) return;
        bookingListBody.innerHTML = "";

        if (bookings.length === 0) {
            bookingListBody.innerHTML = '<tr><td colspan="5" style="text-align:center;">ยังไม่มีรายการจองในขณะนี้</td></tr>';
            return;
        }

        bookings.forEach((booking, index) => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td><strong>${booking.id}</strong></td>
                <td>${booking.name} (${booking.idCode})</td>
                <td>${booking.slot}</td>
                <td><span style="color: #2ecc71; font-weight: bold;">จองแล้ว</span></td>
                <td><button class="btn-cancel" data-index="${index}">ยกเลิก</button></td>
            `;
            bookingListBody.appendChild(row);
        });

        document.querySelectorAll(".btn-cancel").forEach((button) => {
            button.addEventListener("click", function () {
                cancelBooking(Number(this.getAttribute("data-index")));
            });
        });
    }

    function cancelBooking(index) {
        if (index < 0 || index >= bookings.length) return;

        if (confirm(`คุณต้องการยกเลิกรายการจองเครื่อง ${bookings[index].id} ใช่หรือไม่?`)) {
            bookings.splice(index, 1);
            renderComputers();
            renderBookingsTable();
        }
    }

    if (labBookingForm) {
        labBookingForm.addEventListener("submit", function (event) {
            event.preventDefault();

            if (!selectedPcId) {
                alert("กรุณาคลิกเลือกเครื่องคอมพิวเตอร์จากผังด้านบนก่อนครับ");
                return;
            }

            const name = (studentNameInput ? studentNameInput.value : "").trim();
            const studentId = document.getElementById("studentId").value.trim();
            const slot = document.getElementById("timeSlot").value;
            const purpose = document.getElementById("purpose").value.trim();

            if (!name || !studentId || !slot || !purpose) {
                alert("กรุณากรอกข้อมูลให้ครบถ้วนก่อนยืนยันการจอง");
                return;
            }

            bookings.push({
                id: selectedPcId,
                name: name,
                idCode: studentId,
                slot: slot,
                purpose: purpose
            });

            alert(`จองเครื่อง ${selectedPcId} สำเร็จเรียบร้อยแล้ว!`);

            selectedPcId = null;
            if (selectedPcInput) {
                selectedPcInput.value = "";
            }
            labBookingForm.reset();

            if (currentUser && currentUser.isLoggedIn && studentNameInput) {
                studentNameInput.value = currentUser.username;
            }

            renderComputers();
            renderBookingsTable();
        });
    }

    const contactForm = document.getElementById("contactForm");
    if (contactForm) {
        contactForm.addEventListener("submit", function (event) {
            event.preventDefault();

            const name = document.getElementById("name").value.trim().replace(/[<>]/g, "");
            const email = document.getElementById("email").value.trim().replace(/[<>]/g, "");
            const message = document.getElementById("message").value.trim().replace(/[<>]/g, "");

            if (!name || !email || !message) {
                alert("กรุณากรอกข้อมูลแจ้งปัญหาให้ครบถ้วน");
                return;
            }

            console.log("บันทึกการแจ้งปัญหา:", { name, email, message });
            alert(`ขอบคุณครับคุณ ${name}\nระบบได้รับรายงานแจ้งปัญหาเรียบร้อยแล้ว! เจ้าหน้าที่จะเข้าดำเนินการโดยเร็วที่สุด`);
            contactForm.reset();
        });
    }

    const scrollTopBtn = document.createElement("button");
    scrollTopBtn.innerHTML = "↑";
    scrollTopBtn.id = "scrollTopBtn";
    scrollTopBtn.title = "Go to top";
    document.body.appendChild(scrollTopBtn);

    window.addEventListener("scroll", function () {
        scrollTopBtn.style.display = window.scrollY > 200 ? "block" : "none";
    });

    scrollTopBtn.addEventListener("click", function () {
        window.scrollTo({ top: 0, behavior: "smooth" });
    });

    let visitorCount = localStorage.getItem("visitorCount");
    visitorCount = visitorCount === null ? 1 : Number(visitorCount) + 1;
    localStorage.setItem("visitorCount", visitorCount);

    const visitorCountElement = document.getElementById("visitorcount");
    if (visitorCountElement) {
        visitorCountElement.textContent = visitorCount;
    }

    if (burgerBtn && navMenu) {
        burgerBtn.addEventListener("click", function () {
            navMenu.classList.toggle("show");
        });
    }

    renderComputers();
    renderBookingsTable();
});

