create table message_message_template
(
    id               bigserial not null
        constraint message_message_template_pkey
            primary key,
    creator_id       varchar,
    updater_id       varchar,
    create_time      timestamp(6) default now(),
    update_time      timestamp(6) default now(),
    creator_name     varchar,
    updater_name     varchar,
    deleted          integer      default 0,
    versions         integer,
    remark           varchar,
    template_title   varchar,
    template_content varchar,
    status           smallint,
    url              varchar
);
comment on table message_message_template is '系统消息模板表';
comment on column message_message_template.id is '主键id';
comment on column message_message_template.creator_id is '创建人id';
comment on column message_message_template.updater_id is '修改人id';
comment on column message_message_template.create_time is '创建时间';
comment on column message_message_template.update_time is '修改时间';
comment on column message_message_template.creator_name is '创建人名称';
comment on column message_message_template.updater_name is '修改人名称';
comment on column message_message_template.deleted is '是否被删除:0.未删除;1.已删除';
comment on column message_message_template.versions is '版本号';
comment on column message_message_template.remark is '备注';
comment on column message_message_template.template_title is '模板标题';
comment on column message_message_template.template_content is '模板内容';
comment on column message_message_template.status is '是否启用: 0.已禁用, 1.已启用';
comment on column message_message_template.url is '跳转路径';

alter table message_message_template
    owner to postgres;

