alter table user
    change profile_image profile_image_url varchar(255);
alter table user
    add column is_deleted BOOLEAN;
