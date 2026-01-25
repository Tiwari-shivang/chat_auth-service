User db schema:
    uid char(36) primary key,
    firstName varchar(50),
    lastName varchar(50),
    userName varchar(30) unique,
    email varchar(100) unique,
    password varchar(256),
    role ENUM('user', 'admin') Default 'user',
    gender ENUM('male', 'female', 'others') Default 'male',
    dob Date Not null,
    isActive TinyInt(1) default 1,
    lastLogin DATETIME,
    createdAt DATETIME,
    updatedAt DATETIME default CURRENT_TIMESTAMP

Preferences db schema:
    pid Char(36) primary key,
    uid Char(36) Not null,
    theme ENUM('light', 'dark', 'custom') default 'light',
    primaryClr varchar(10),
    secondaryClr varchar(10),
    createdAt Datetime default CURRENT_TIMESTAMP,
    updatedAt DATETIME default CURRENT_TIMESTAMP,
    FOREIGN KEY (uid) references users(uid) on delete cascade

Connections db schema:
    conId char(36) primary key,
    receiverId char(36) not null,
    senderId char(36) not null,
    status ENUM('pending', 'deleted', 'accepted') default 'pending',
    createdAt DateTime,
    updatedAt DateTime default CURRENT_TIMESTAMP,
    Foreign key (receiverId) references users(uid) on delete cascade,
    Foreign key (senderId) references users(uid) on delete cascade