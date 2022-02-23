create table user_user
(
    id            bigserial not null
        constraint user_user_pkey
            primary key,
    creator_id    bigint,
    updater_id    bigint,
    creator_name  varchar,
    updater_name  varchar,
    create_time   timestamp default now(),
    update_time   timestamp default now(),
    deleted       integer   default 0,
    versions      integer,
    remark        varchar,
    phone         varchar,
    head_portrait varchar,
    gender        integer,
    province_code varchar,
    city_code     varchar,
    area_code     varchar,
    province_name varchar,
    city_name     varchar,
    area_name     varchar,
    address       varchar,
    birthday      date,
    occupation    varchar,
    nickname   varchar,
    nation        varchar   default '汉族'::character varying,
    hometown      varchar,
    age           integer
);

comment on table user_user is '用户表';
comment on column user_user.id is '主键id';
comment on column user_user.creator_id is '创建人id';
comment on column user_user.updater_id is '修改人id';
comment on column user_user.creator_name is '创建人名称';
comment on column user_user.updater_name is '修改人名称';
comment on column user_user.create_time is '创建时间';
comment on column user_user.update_time is '修改时间';
comment on column user_user.deleted is '是否被删除:0.未删除;1.已删除';
comment on column user_user.versions is '版本号';
comment on column user_user.remark is '备注';

comment on column user_user.phone is '手机号码';
comment on column user_user.head_portrait is '头像';
comment on column user_user.gender is '性别:0.男；1.女;';
comment on column user_user.province_code is '现住地省编码';
comment on column user_user.city_code is '现住地市编码';
comment on column user_user.area_code is '现住地区编码';
comment on column user_user.province_name is '现住地省名称';
comment on column user_user.city_name is '现住地市名称';
comment on column user_user.area_name is '现住地区名称';
comment on column user_user.address is '现住地详细地址';
comment on column user_user.birthday is '生日';
comment on column user_user.occupation is '职业';
comment on column user_user.nickname is '昵称';
comment on column user_user.nation is '民族';
comment on column user_user.hometown is '家乡';
comment on column user_user.age is '年龄';

alter table user_user
    owner to postgres;

