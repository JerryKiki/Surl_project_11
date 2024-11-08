show databases;

drop database if exists surl_dev;
create database surl_dev;
use surl_dev;

show tables;
desc article;

select * from article;
select * from member;

TRUNCATE TABLE article;
TRUNCATE TABLE `member`;