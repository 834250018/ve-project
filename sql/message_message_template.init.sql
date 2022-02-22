INSERT INTO public.message_message_template (id, creator_id, updater_id, create_time, update_time, creator_name,
                                             updater_name, deleted, versions, remark, template_title, template_content,
                                             status, route_uri)
VALUES (0, null, null, DEFAULT, DEFAULT, null, null, 0, null, null, '#month#月通知', '您#month#月的余额为#money#元', 0, null);

