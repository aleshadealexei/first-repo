create table user_podpiski (
    sub_id int8 not null references userdata,
    channel_id int8 not null references userdata,
    primary  key (sub_id, channel_id)
);