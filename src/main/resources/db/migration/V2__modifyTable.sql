alter table expense
    add column payer_id bigint;

alter table expense
    add constraint FK_expense_payer_id
        foreign key (payer_id)
            references trip_user (id);
