create table country (
    id bigint not null auto_increment,
    continent enum ('ASIA','AFRICA','NORTH_AMERICA','SOUTH_AMERICA','OCEANIA','EUROPE'),
    cover_image_url varchar(255),
    flag_image_url varchar(255),
    name varchar(255),
    primary key (id)
) engine=InnoDB;

create table currency_unit (
    id bigint not null auto_increment,
    name varchar(255),
    symbol varchar(255),
    country_id bigint,
    primary key (id)
) engine=InnoDB;

create table expense (
    id bigint not null auto_increment,
    amount float(53),
    expense_category enum ('FOOD','TRANSPORT','LODGE','TRAVEL','ACTIVITY','FLIGHT','SHOPPING','ETC'),
    expense_method enum ('CARD','CASH'),
    name varchar(255),
    payed_at datetime(6),
    expense_type enum ('SHARED','INDIVIDUAL','SHARED_BUDGET_INCOME','INDIVIDUAL_BUDGET_EXPENSE'),
    trip_id bigint,
    unit_id bigint,
    primary key (id)
) engine=InnoDB;

create table expense_photo (
    id bigint not null auto_increment,
    created_at datetime(6),
    modified_at datetime(6),
    image_url varchar(255),
    expense_id bigint,
    primary key (id)
) engine=InnoDB;

create table trip (
    id bigint not null auto_increment,
    created_at datetime(6),
    modified_at datetime(6),
    individual_budget bigint,
    shared_budget bigint,
    end_date datetime(6),
    start_date datetime(6),
    title varchar(255),
    primary key (id)
) engine=InnoDB;

create table trip_country (
    id bigint not null auto_increment,
    exchange_rate_standard bigint,
    exchange_rate_type enum ('CUSTOM','AUTO'),
    exchange_rate_value float(53),
    country_id bigint,
    currency_unit_id bigint,
    trip_id bigint,
    primary key (id)
) engine=InnoDB;

create table trip_user (
    id bigint not null auto_increment,
    name varchar(255),
    trip_user_state enum ('CONNECTED','UNCONNECTED'),
    trip_id bigint,
    user_id bigint,
    primary key (id)
) engine=InnoDB;

create table user_expense (
    id bigint not null auto_increment,
    amount float(53),
    expense_id bigint,
    trip_user_id bigint,
    primary key (id)
) engine=InnoDB;

alter table currency_unit
    add constraint FK_currency_unit_country_id
        foreign key (country_id)
            references country (id);

alter table expense
    add constraint FK_expense_trip_id
        foreign key (trip_id)
            references trip (id);

alter table expense
    add constraint FK_expense_unit_id
        foreign key (unit_id)
            references currency_unit (id);

alter table expense_photo
    add constraint FK_expense_photo_expense_id
        foreign key (expense_id)
            references expense (id);

alter table trip_country
    add constraint FK_trip_country_country_id
        foreign key (country_id)
            references country (id);

alter table trip_country
    add constraint FK_trip_country_currency_unit_id
        foreign key (currency_unit_id)
            references currency_unit (id);

alter table trip_country
    add constraint FK_trip_country_trip_id
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
