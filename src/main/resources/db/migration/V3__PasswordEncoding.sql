create extension if not exists pgcrypto;

update userdata set password = crypt(password, gen_salt('bf', 8));