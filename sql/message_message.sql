create table message_message
(
    id           bigserial not null
        constraint message_message_pkey
            primary key,
    creator_id   bigint,
    updater_id   bigint,
    create_time  timestamp(6) default now(),
    update_time  timestamp(6) default now(),
    creator_name varchar,
    updater_name varchar,
    deleted      integer      default 0,
    versions     integer,
    remark       varchar,
    user_id      bigint,
    title        varchar,
    content      varchar,
    status       smallint,
    detail_id    bigint,
    url          varchar,
    template_id  bigint,
    param        varchar
);
comment on table message_message is '系统消息通知表';
comment on column message_message.id is '主键id';
comment on column message_message.creator_id is '创建人id';
comment on column message_message.updater_id is '修改人id';
comment on column message_message.create_time is '创建时间';
comment on column message_message.update_time is '修改时间';
comment on column message_message.creator_name is '创建人名称';
comment on column message_message.updater_name is '修改人名称';
comment on column message_message.deleted is '是否被删除:0.未删除;1.已删除';
comment on column message_message.versions is '版本号';
comment on column message_message.remark is '备注';

comment on column message_message.user_id is '接收通知的用户id';
comment on column message_message.title is '通知标题';
comment on column message_message.content is '通知内容';
comment on column message_message.status is '是否已读: 0.未读, 1.已读';
comment on column message_message.detail_id is '详情id,跳转后的页面详情id';
comment on column message_message.url is '跳转路径';
comment on column message_message.template_id is '模板id';
comment on column message_message.param is '参数';

alter table message_message
    owner to postgres;

