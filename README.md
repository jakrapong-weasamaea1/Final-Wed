<!DOCTYPE html>
<html lang="th">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="FTU IT Laboratory Computer Booking System - ระบบจองใช้งานคอมพิวเตอร์ห้องปฏิบัติการไอที มหาวิทยาลัยฟาฏอนี">
    <title>FTU IT Lab Computer Reservation System</title>
    
    
    <link rel="stylesheet" href="style.css">
    
    <style>
        
        .lab-layout {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(90px, 1fr));
            gap: 15px;
            margin: 20px 0;
            padding: 20px;
            background: #f4f6f9;
            border-radius: 8px;
        }
        .computer-card {
            padding: 20px 10px;
            text-align: center;
            border-radius: 8px;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.2s ease;
            user-select: none;
            box-shadow: 0 2px 5px rgba(0,0,0,0.08);
        }
        .computer-card.available { background-color: #2ecc71; color: white; }
        .computer-card.available:hover { opacity: 0.85; transform: translateY(-2px); }
        .computer-card.booked { background-color: #e74c3c; color: white; cursor: not-allowed; opacity: 0.6; }
        .computer-card.selected { background-color: #f39c12; color: white; transform: scale(1.05); }
        
        .status-legend {
            display: flex;
            gap: 20px;
            margin-bottom: 15px;
            font-size: 0.95em;
        }
        .legend-item { display: flex; align-items: center; gap: 8px; }
        .dot-icon { width: 14px; height: 14px; border-radius: 50%; display: inline-block; }
        .dot-available { background-color: #2ecc71; }
        .dot-booked { background-color: #e74c3c; }
        .dot-selected { background-color: #f39c12; }
        
        .booking-summary-table { width: 100%; margin-top: 15px; border-collapse: collapse; }
        .booking-summary-table th, .booking-summary-table td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        .btn-cancel { background-color: #e74c3c; color: white; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer; }
        .btn-cancel:hover { background-color: #c0392b; }
    </style>
</head>
<body>

    
    <header class="header-container">
        <div class="header-top">
            <h1 class="main-title">IT Lab Computer Booking</h1>
            <button class="burger-btn" id="burgerBtn" aria-label="Toggle menu">☰</button>
        </div>
        <p class="subtitle">Computer Reservation System for the Information Technology Laboratory at Fatoni University</p>

        <nav class="nav-menu" id="navMenu">
    <a href="#lab-status">Computer Layout</a>
    <a href="#booking-form">Booking Form</a>
    <a href="#active-bookings">My Booking List</a>
    <a href="#report-issue">Report Issue / Contact Admin</a>
    <a href="login.html">Login</a>
</nav>
    </header>

    <main class="content-container">

        
        <section class="cv-section" id="lab-status">
    <h2 class="section-title">Select the computer you want to use</h2>
    <p>Please click to select the computer you want to book from the layout below:</p>

    <div class="status-legend">
        <span class="legend-item"><span class="dot-icon dot-available"></span> Available</span>
        <span class="legend-item"><span class="dot-icon dot-booked"></span> Unavailable / Booked</span>
        <span class="legend-item"><span class="dot-icon dot-selected"></span> Selected</span>
    </div>

    <div class="lab-layout" id="computerGrid"></div>
        </section>

        
        <section class="cv-section" id="booking-form">
    <h2 class="section-title">Booking Form</h2>
    <form id="labBookingForm" class="contact-form">
        <div class="form-group">
            <label for="selectedPc">Selected Computer:</label>
            <input type="text" id="selectedPc" class="form-input" placeholder="Please click a computer from the layout above" readonly required>
        </div>

        <div class="form-group">
            <label for="studentName">Full Name of Service User:</label>
            <input type="text" id="studentName" class="form-input" placeholder="Enter full name" required>
        </div>
                
        <div class="form-group">
    <label for="studentId">Student ID / Personal Identification Number:</label>
    <input type="text" id="studentId" class="form-input" placeholder="e.g. 66xxxxxxxx" required>
</div>

<div class="form-group">
    <label for="timeSlot">Preferred Time Slot:</label>
    <select id="timeSlot" class="form-input" required style="width: 100%; padding: 10px;">
        <option value="">-- Select a time slot --</option>
        <option value="09:00 - 11:00">09:00 - 11:00</option>
        <option value="11:00 - 13:00">11:00 - 13:00</option>
        <option value="13:00 - 15:00">13:00 - 15:00</option>
        <option value="15:00 - 17:00">15:00 - 17:00</option>
    </select>
</div>

                <div class="form-group">
    <label for="purpose">Purpose / Software to be Used:</label>
    <textarea id="purpose" class="form-input form-textarea" rows="3" placeholder="Specify the purpose, e.g. web development, laboratory practice..." required></textarea>
</div>

<button type="submit" class="submit-btn">Confirm Booking</button>
</form>
</section>

<section class="cv-section" id="active-bookings">
    <h2 class="section-title">Current Booking List</h2>
    <table class="custom-table booking-summary-table">
        <thead>
                    <tr>
                        <th>Computer ID</th>
                        <th>Booker Name</th>
                        <th>Time Slot</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody id="bookingListBody">
                    
                </tbody>
            </table>
        </section>

        
        <section class="cv-section" id="report-issue">
    <h2 class="section-title">Report Equipment Issues / Contact Lab Administrator</h2>
    <form id="contactForm" class="contact-form" action="#" method="post">
        <div class="form-group">
            <label for="name">Reporter Name:</label>
            <input type="text" id="name" name="name" class="form-input" placeholder="Enter your name" required>
        </div>

        <div class="form-group">
            <label for="email">Contact Email:</label>
            <input type="email" id="email" name="email" class="form-input" placeholder="Enter your email" required>
        </div>

        <div class="form-group">
            <label for="message">Problem Details:</label>
            <textarea id="message" name="message" class="form-input form-textarea" rows="3" placeholder="Specify the computer and issue found (e.g. monitor not working, mouse broken...)" required></textarea>
        </div>

                <button type="submit" class="submit-btn">Submit Report</button>
            </form>
        </section>

    </main>

   <footer class="footer-container">
    <p>&copy; 2026 FTU IT Laboratory Management System. All rights reserved.</p>
    <p>Total visitors: <span id="visitorcount">0</span> people</p>
</footer>

<script src="script.js"></script>
</body>
</html>
