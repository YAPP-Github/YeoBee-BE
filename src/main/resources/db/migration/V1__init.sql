create table auth_provider
(
    id                  bigint not null auto_increment,
    apple_refresh_token varchar(255),
    social_login_id     varchar(255),
    type                enum ('KAKAO','APPLE'),
    user_id             bigint,
    primary key (id)
) engine = InnoDB;

create table country
(
    name            varchar(255) not null,
    continent       enum ('ASIA','AFRICA','NORTH_AMERICA','SOUTH_AMERICA','OCEANIA','EUROPE'),
    cover_image_url varchar(255),
    flag_image_url  varchar(255),
    primary key (name)
) engine = InnoDB;

create table country_currency
(
    id            bigint not null auto_increment,
    country_name  varchar(255),
    currency_code varchar(255),
    primary key (id)
) engine = InnoDB;

create table currency
(
    code varchar(255) not null,
    name varchar(255),
    primary key (code)
) engine = InnoDB;

create table expense
(
    id               bigint not null auto_increment,
    amount           decimal(38, 2),
    expense_category enum ('FOOD','TRANSPORT','LODGE','TRAVEL','ACTIVITY','FLIGHT','SHOPPING','ETC'),
    expense_method   enum ('CARD','CASH'),
    expense_type     enum ('SHARED','INDIVIDUAL','SHARED_BUDGET_INCOME','INDIVIDUAL_BUDGET_EXPENSE'),
    name             varchar(255),
    payed_at         datetime(6),
    trip_id          bigint,
    currency_code    bigint,
    trip_currency_id bigint,
    primary key (id)
) engine = InnoDB;

create table expense_photo
(
    id          bigint not null auto_increment,
    created_at  datetime(6),
    modified_at datetime(6),
    image_url   varchar(255),
    expense_id  bigint,
    primary key (id)
) engine = InnoDB;

create table refresh_token
(
    user_id bigint not null,
    value   varchar(255),
    primary key (user_id)
) engine = InnoDB;

create table trip
(
    id                bigint not null auto_increment,
    created_at        datetime(6),
    modified_at       datetime(6),
    individual_budget bigint,
    shared_budget     bigint,
    end_date          datetime(6),
    start_date        datetime(6),
    title             varchar(255),
    primary key (id)
) engine = InnoDB;

create table trip_country
(
    id           bigint not null auto_increment,
    country_name varchar(255),
    trip_id      bigint,
    primary key (id)
) engine = InnoDB;

create table trip_currency
(
    id                     bigint not null auto_increment,
    exchange_rate_standard bigint,
    exchange_rate_type     enum ('CUSTOM','AUTO'),
    exchange_rate_value    decimal(38, 2),
    currency_code          varchar(255),
    trip_id                bigint,
    primary key (id)
) engine = InnoDB;

create table trip_user
(
    id              bigint not null auto_increment,
    name            varchar(255),
    trip_user_state enum ('CONNECTED','UNCONNECTED'),
    trip_id         bigint,
    user_id         bigint,
    primary key (id)
) engine = InnoDB;

create table user
(
    id                bigint not null auto_increment,
    created_at        datetime(6),
    modified_at       datetime(6),
    is_deleted        bit    not null,
    nickname          varchar(255),
    profile_image_url varchar(255),
    primary key (id)
) engine = InnoDB;

create table user_expense
(
    id           bigint not null auto_increment,
    amount       decimal(38, 2),
    expense_id   bigint,
    trip_user_id bigint,
    primary key (id)
) engine = InnoDB;

alter table auth_provider
    add constraint UK_social_login_id unique (social_login_id);

alter table auth_provider
    add constraint FK_auth_provider_user_id
        foreign key (user_id)
            references user (id);

alter table country_currency
    add constraint FK_country_currency_country_name
        foreign key (country_name)
            references country (name);

alter table country_currency
    add constraint FK_country_currency_currency_code
        foreign key (currency_code)
            references currency (code);

alter table expense
    add constraint FK_expense_trip_id
        foreign key (trip_id)
            references trip (id);

alter table expense
    add constraint FK_expense_trip_currency_id
        foreign key (trip_currency_id)
            references trip_currency (id);

alter table expense_photo
    add constraint FK_expense_photo_expense_id
        foreign key (expense_id)
            references expense (id);

alter table trip_country
    add constraint FK_trip_country_country_name
        foreign key (country_name)
            references country (name);

alter table trip_country
    add constraint FK_trip_country_trip_id
        foreign key (trip_id)
            references trip (id);

alter table trip_currency
    add constraint FK_trip_currency_currency_code
        foreign key (currency_code)
            references currency (code);

alter table trip_currency
    add constraint FK_trip_currency_trip_id
        foreign key (trip_id)
            references trip (id);

alter table trip_user
    add constraint FK_trip_user_trip_id
        foreign key (trip_id)
            references trip (id);

alter table trip_user
    add constraint FK_trip_user_user_id
        foreign key (user_id)
            references user (id);

alter table user_expense
    add constraint FK_user_expense_expense_id
        foreign key (expense_id)
            references expense (id);

alter table user_expense
    add constraint FK_user_expense_trip_user_id
        foreign key (trip_user_id)
            references trip_user (id);