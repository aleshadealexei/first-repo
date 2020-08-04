create table message_likes(
    message_id int8 references message,
    user_id int8 references userdata,
    primary key (message_id, user_id)
);