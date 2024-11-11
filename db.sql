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

--##### postgre

drop database surl_dev;
create database surl_dev;
-- use 대신 edit connection에서 사용중인 db명을 바꿔줘야 함

-- show databases;
select datname from pg_database

-- 현재 선택된 데이터베이스안의 모든 사용자 정의 테이블 목록 나열
SELECT tablename
FROM pg_catalog.pg_tables
WHERE schemaname != 'pg_catalog'
AND schemaname != 'information_schema';

--post 테이블 생성
create table post (
                      id bigserial not null,
                      primary key (id),
                      create_date timestamp(6),
                      modify_date timestamp(6),
                      title varchar(255)
)


--post 테이블에 데이터 삽입
--# V1
insert into post
(id, create_date, modify_date, title)
values
(default, NOW(), NOW(), '제목 1')

--# v2
insert into post
(create_date, modify_date, title)
values
(NOW(), NOW(), '제목 1')

--검색어가 당근 이고, 1 페이지에 보여줄 글 가져오기
--한 페이지에 최대 10개의 글 노출 가능

--데이터 쿼리
select P.*
from post AS P
where upper(P.title) like upper('%당근%') escape '\'
order by P.id desc
    limit 10 offset 0;

--카운트 쿼리
select count(P.id)
from post AS P
where upper(P.title) like upper('%당근%') escape '\'

--검색어가 당근 이고, 2 페이지에 보여줄 글 가져오기
--한 페이지에 최대 10개의 글 노출 가능

select P.*
from post AS P
where upper(P.title) like upper('%당근%') escape '\'
order by P.id desc
    limit 10 offset 10;
--카운트 쿼리
select count(P.id)
from post AS P
where upper(P.title) like upper('%당근%') escape '\'