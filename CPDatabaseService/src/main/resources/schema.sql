create table loans (
    loan_id         bigint auto_increment,
    loan_amount     numeric(8,2),
    term            integer,
    status          char(1),
    primary key ( loan_id )
);

create table payments (
    payment_id      bigint auto_increment,
    payment_amount  numeric(8,2),
    payment_date    timestamp,
    loan_id         bigint,
    primary key ( payment_id ),
    foreign key ( loan_id ) references loans( loan_id )
);