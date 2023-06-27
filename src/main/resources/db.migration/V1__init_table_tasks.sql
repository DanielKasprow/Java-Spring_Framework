drop table if exists kurs.tasks;
create table kurs.tasks
(
    id          int primary key auto_increment,
    description varchar(100) not null,
    done        bit
)