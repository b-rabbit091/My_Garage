# MyGarage  
A Vehicle Expense, Maintenance, and Reminder Tracking App

---

## 1. Overview
MyGarage is an Android application designed to help users organize and manage all important information related to their vehicles. Many people forget service dates, lose track of fuel or repair expenses, or misplace receipts. This app solves those problems by providing a simple and structured system where users can store vehicle details, record expenses, set maintenance reminders, and view spending summaries.

MyGarage keeps all data stored safely on the device using Room (SQLite), ensuring privacy while offering fast access to information. The app also provides weekly, monthly, and yearly reports, helping users understand their spending patterns and make better decisions about vehicle maintenance.

---

## 2. Main Features
- **User Accounts:** Sign up, log in, private local data.
- **Dashboard:** Quick access to all app functions.
- **Vehicle Management:** Add/update vehicle name, model, make, purchase date, mileage.
- **Expense Tracking:** Fuel, washing, servicing, and other expenses (amount + date).
- **Reminders:** Fuel, washing, and service reminders with date/time and optional notes.
- **Vehicle Details:** Full overview, expense history, total cost.
- **Reports:** Weekly, monthly, and yearly summaries.
- **Notifications:** AlarmManager for reminder alerts.

---

## 3. Data Storage
Room (SQLite) is used to store:
- User accounts  
- Vehicles  
- Expenses  
- Reminders  
- Summary data  
All information remains local to the device.

---

## 4. Technologies Used
- Android Studio  
- Java  
- XML Layouts  
- Room (SQLite)  
- Material Components  
- AlarmManager  
- DatePicker & TimePicker  
- Intents for navigation  

---

## 5. Screenshots

### Login Screen  
<img width="366" height="730" alt="Login Screen" src="https://github.com/user-attachments/assets/9b5a559c-8420-49e0-94cf-2abe241d6b18" />


### Dashboard  
<img width="355" height="721" alt="Dashboard" src="https://github.com/user-attachments/assets/f4b6a7ec-f7e1-4865-a780-ba77caba6f8a" />


### Add / Modify Vehicle  
<img width="355" height="730" alt="Add : Modify Vehicle" src="https://github.com/user-attachments/assets/35fdf368-5ee3-4a93-851f-13ff2ac53ede" />


### Add Expenses  
<img width="357" height="737" alt="Add Expenses" src="https://github.com/user-attachments/assets/d2a76114-2144-4c75-9b5a-ae145af04449" />


### Set Reminders  
<img width="343" height="718" alt="Set Reminders" src="https://github.com/user-attachments/assets/24b602a5-9cec-4a07-9bb0-667be684a058" />


### Vehicle Details  
<img width="351" height="730" alt="Vehicle Details" src="https://github.com/user-attachments/assets/f0891d27-f279-4d11-b0a6-93037da1e38e" />


### Reminder List  
<img width="353" height="724" alt="Reminder List" src="https://github.com/user-attachments/assets/322834ac-2b5c-48f8-b768-1d69345a3fd8" />


### Reports  
<img width="357" height="727" alt="Reports" src="https://github.com/user-attachments/assets/866eb08a-d511-48aa-812a-2a2099659167" />

---
## 6. Build & Run
### Requirements
- Android Studio (latest)  
- Android device or emulator  

### Steps

1. Open in Android Studio  
2. Wait for Gradle sync  
3. Run on emulator or device  

---

## 7. Test Accounts for Grading

Instructor
Email: instructor@mygarage.test

Password: 1234

GA
Email: ga@mygarage.test

Password: 1234

Demo User 1
Email: demo1@mygarage.test

Password: 1234

Demo User 2
Email: demo2@mygarage.test

Password: 1234


---

## 8. Completion Status (Based on Project Proposal)

| **Features**                       | **Status**    |
|-----------------------------------|---------------|
| User Sign-Up & Login              | Completed     |
| Dashboard Summary                 | Completed     |
| Vehicle Management                | Completed     |
| Expense Tracking & Categorization | Completed     |
| Reminder Scheduling               | Completed     |
| Reports (Weekly / Monthly / Yearly) | Completed   |
| Local Data Storage (Room DB)     | Completed     |
| UI/UX Enhancements               | Completed     |
| Full App Navigation               | Completed     |
| Test User Credentials             | Added         |

---

## 9. Internal Documentation

The codebase includes:
- Clear variable and method naming  
- Comments explaining important logic  
- Organized Activities and database structure  
- Consistent UI layout files  
- Readable and maintainable structure  

---

## 10. Problems Faced & Solutions

- **Data transfer between Activities**  
  Resolved using Intent extras and proper object handling.  

- **Reminder scheduling inconsistencies**  
  Fixed through correct integration of DatePicker, TimePicker, and AlarmManager.  

- **Layout scaling on different screen sizes**  
  Solved using ConstraintLayout and responsive design principles.  

- **Expense-to-vehicle mapping issues**  
  Addressed by assigning unique vehicle IDs in the Room database.  

- **Form input validation errors**  
  Resolved by adding mandatory field checks.  

---

## 11. Future Improvements

- Cloud data sync and backup  
- Export reports to PDF or Excel  
- Upload and store documents (registration, insurance, receipts)  
- Advanced graphing and analytics  

---

## 12. Team Members

- Jhansi Akshara Sanagala  
- Chaithanya Reddy Pailla  
- Charan Reddy Chilipireddy  
- Anil Panday
  

---



