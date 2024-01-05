create table auth_provider (
    id bigint not null auto_increment,
    user_id bigint,
    apple_refresh_token varchar(255),
    social_login_id varchar(255),
    type enum ('KAKAO','APPLE'),
    primary key (id)
) engine=InnoDB;

create table refresh_token (
    user_id bigint not null,
    value varchar(255),
    primary key (user_id)
) engine=InnoDB;

create table user (
    created_at datetime(6),
    id bigint not null auto_increment,
    modified_at datetime(6),
    nickname varchar(255),
    profile_image varchar(255),
    primary key (id)
) engine=InnoDB;

alter table auth_provider
   add constraint UK_social_login_id unique (social_login_id);

alter table auth_provider
   add constraint FK_auth_provider_user_id
   foreign key (user_id)
   references user (id);
