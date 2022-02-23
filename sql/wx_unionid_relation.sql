create table wx_unionid_relation
(
    id           bigserial not null
        constraint wx_unionid_relation_pkey
            primary key,
    creator_id   bigint,
    updater_id   bigint,
    creator_name varchar,
    updater_name varchar,
    create_time  timestamp default now(),
    update_time  timestamp default now(),
    deleted      integer   default 0,
    versions     integer,
    remark       varchar,
    openid       varchar,
    unionid      varchar,
    status       smallint
);

comment on table wx_unionid_relation is '微信公众号openid关联表';
comment on column wx_unionid_relation.id is '主键id';
comment on column wx_unionid_relation.creator_id is '创建人id';
comment on column wx_unionid_relation.updater_id is '修改人id';
comment on column wx_unionid_relation.creator_name is '创建人名称';
comment on column wx_unionid_relation.updater_name is '修改人名称';
comment on column wx_unionid_relation.create_time is '创建时间';
comment on column wx_unionid_relation.update_time is '修改时间';
comment on column wx_unionid_relation.deleted is '是否被删除:0.未删除;1.已删除';
comment on column wx_unionid_relation.versions is '版本号';
comment on column wx_unionid_relation.remark is '备注';
comment on column wx_unionid_relation.openid is '用户公众号openid';
comment on column wx_unionid_relation.unionid is '用户unionid';
comment on column wx_unionid_relation.status is '订阅状态: 0.取消订阅,1.订阅中';

alter table wx_unionid_relation
    owner to postgres;

