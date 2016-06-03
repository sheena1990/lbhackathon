drop table location;

drop table help_user;

create table location(
location_id number not null primary key,
latitude number,
longitude number
);


create table help_user(
user_token varchar2(100) not null primary key,
imei_num varchar2(100),
last_name varchar(20),
first_name varchar2(20),
phone_num varchar2(100),
location_id number,
constraint fk_user_location foreign key(location_id) references location(location_id)
);
