# Proposal
## Description
We are building a program to generate a weekly chore schedule for the Management Team. 
The purpose of the Chore Management System is to automatically create a new chore schedule 
and scorecard each week. Each user will be assigned to what chore they are doing on that 
day. Managers check off on chores. The scorecard is used to measure individual and group performance. 

There are 5 different chores: kitchen, floors, bathrooms, lobby, and trash. Two people 
are assigned one chore per day of the week. One user is only able to work two chores per week. Each 
chore should be checked off by a manager. A manager can only check off 3 chores a week. Users and 
Managers should be notified both when a new schedule is uploaded and when a chore is not completed. 
Managers don't do chores.

## Prior Art
The Management Team usually creates all schedules by hand which take time. Especially if one can 
not do a particular chore that day. We are developing a similar way of creating those schedules 
without having to remake the entire thing. We attempted to look for similar projects for job scheduling
but could not find any web applications in spring.

## Core User Workflows
1. Login - login to an account
2. Schedules -  Users can sign in and see the schedule for this week and following weeks.
   - Manager limitations - Managers can to check off 3 chores
   - User limitations - Users can only work 2 days a week
4. Adjusting Schedules - Managers can edit the schedule
5. Email - Users will receive an email about the schedule
6. Scorecards - Everyone will receive a scorecard with a performance percentage
7. Create Accounts - Admin can create accounts and decide if they are a manager or an employee

## Weekly Goals
### Week 1 (July 12-16)
#### Bryce:
- Login and Signup / Create Accounts
- I will work on user authentication where the user can login to see the schedules. I will be working on the users table to save login information to the database. Also, I will be working on the Admin and Manager page. 

- The Admin can create a user
  
- I will also set up our home page and add minimal styling.

#### Kenia:
- I plan to work on our schedule table in the database to save the schedule information. I am also going to work on the schedule class and form. Then I will work on making user accounts for the scheduling. Eventually, I will meet with Bryce to talk about styling. 
#### Both:
- Meet with client for updates and discuss what we can change 

### Week 2 (July 19-23)
#### Bryce:
- Schedules(Admin)
- I will be working on the users schedule. Then I'll work on how they are only assigned two days of chores per week.
#### Kenia: 
- Schedules(Management)
- I will continue styling until Bryce finishes the users schedule, and then add the manager check off to that schedule.

#### Both:
- Both of us are going to work on styling the schedule into more of a calendar view.
- Meet with client for updates and discuss what we can change

### Week 3 (July 26-30)

#### Bryce: 
- Email(User)
- I will be working on the emailing portion of the project. That way they are notified when there is a new schedule.
#### Kenia: 
- Email
- I am going to work on emailing users when chores are missed for the day, and also marking the schedule as missed.

#### Both:
- Meet with client for updates and discuss what we can change

### Week 4 (August 2-6)
#### Bryce:
- Scorecards(Overall)
- I will be working on the overall group performance on the scorecard. I also will be working on some styling as well.
#### Kenia: 
- Scorecards(Manager)
- Scorecard(User)
- I am going to work on both the individual and managers scorecard and any additional css.
#### Both:
- We are going to dedicate our time to styling the appearance of the web page
- We are also going to work on the bread crumb trail to make sure navigation through the web app is efficient. 
  
## Team
- Bryce Taylor
- Kenia Sandoval-Lopez

## Technologies
- Spring Boot
- Html/CSS
- Java
- MySQL

## GitHub Repository 
https://github.com/Bryce-Taylor/Capstone-Project
