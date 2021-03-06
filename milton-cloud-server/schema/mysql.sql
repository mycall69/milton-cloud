alter table access_log drop foreign key FKBBF335E9C4027D76;
alter table access_log drop foreign key FKBBF335E91D457434;
alter table address_book drop foreign key FKFB8B0542CB36CA7;
alter table app_control drop foreign key FKEE00D53FC4027D76;
alter table app_control drop foreign key FKEE00D53F1CD5CA34;
alter table app_control drop foreign key FKEE00D53F849B88AA;
alter table app_setting drop foreign key FK2BA87E323BD6907C;
alter table base_email_job drop foreign key FK2F3D58ACC4027D76;
alter table branch drop foreign key FKADAF25A2EDBCEC59;
alter table branch drop foreign key FKADAF25A2A0FA3AD6;
alter table branch drop foreign key FKADAF25A2DE9B19E5;
alter table cal_event drop foreign key FKABCCC2E9C9A2A33E;
alter table cal_event_process drop foreign key FK68508F19CBDEA377;
alter table cal_event_process drop foreign key FK68508F197E512317;
alter table calendar drop foreign key FKF55EFB3E75CB2D09;
alter table certificate drop foreign key FK745F4197C4027D76;
alter table comment drop foreign key FK38A5EE5F889319BC;
alter table commit_item drop foreign key FK5A19351B2AEC5E14;
alter table commit_item drop foreign key FK5A19351B99AB4086;
alter table contact drop foreign key FK38B7242012BCE54F;
alter table contact_extended_property drop foreign key FK8C45B7BCFCBFFB3E;
alter table credential drop foreign key FKD743E857666EC650;
alter table email_item drop foreign key FK4E94C9D642E056DC;
alter table email_item drop foreign key FK4E94C9D6A058AC6F;
alter table email_item drop foreign key FK4E94C9D67DB36992;
alter table email_item drop foreign key FK4E94C9D6AACF6B30;
alter table email_send_attempt drop foreign key FK7EF81CD9C331BB44;
alter table email_trigger drop foreign key FKD75DEC15AACDDC8E;
alter table enrolement drop foreign key FK688BE7D1D457434;
alter table enrolement drop foreign key FK688BE7D73B79E62;
alter table fanout_entry drop foreign key FKD6C6228E4FF762A6;
alter table forum drop foreign key FK5D18D211D457434;
alter table forum_post drop foreign key FKCDDDD0FE889319BC;
alter table forum_post drop foreign key FKCDDDD0FE8F132661;
alter table forum_reply drop foreign key FKEDF3E64C889319BC;
alter table forum_reply drop foreign key FKEDF3E64C70AD9EA0;
alter table group_email_job drop foreign key FKF4C306DAAACDDC8E;
alter table group_entity drop foreign key FKDC016DA3C4027D76;
alter table group_in_website drop foreign key FK121949416A085208;
alter table group_in_website drop foreign key FK121949411D457434;
alter table group_membership drop foreign key FK526319638A2C741;
alter table group_membership drop foreign key FK5263196D360BEA0;
alter table group_membership drop foreign key FK526319630DCD1AC;
alter table group_membership_application drop foreign key FK88BAE42738A2C741;
alter table group_membership_application drop foreign key FK88BAE427D360BEA0;
alter table group_membership_application drop foreign key FK88BAE42730DCD1AC;
alter table group_membership_application drop foreign key FK88BAE4271D457434;
alter table group_membership_application drop foreign key FK88BAE4278B021E90;
alter table group_recipient drop foreign key FKA73C90592840C316;
alter table group_recipient drop foreign key FKA73C9059AACF6B30;
alter table group_reward drop foreign key FKF1B2D8EF1FA8C924;
alter table group_reward drop foreign key FKF1B2D8EF111E7CB0;
alter table group_role drop foreign key FK4C707A3630DCD1AC;
alter table group_role drop foreign key FK4C707A36A0FA3AD6;
alter table group_role drop foreign key FK4C707A3681447B9;
alter table learner_process drop foreign key FKADA71E21CBDEA377;
alter table learner_process drop foreign key FKADA71E21666EC650;
alter table learning_log drop foreign key FKAA3686A3C4027D76;
alter table learning_log drop foreign key FKAA3686A31D457434;
alter table learning_log drop foreign key FKAA3686A3666EC650;
alter table membership_process drop foreign key FKC4F81706CBDEA377;
alter table module_status drop foreign key FKA7AA55251D457434;
alter table module_status drop foreign key FKA7AA5525666EC650;
alter table module_status_field drop foreign key FK7F08296042C9B4E9;
alter table module_status_process drop foreign key FKFE862555CBDEA377;
alter table module_status_process drop foreign key FKFE86255542C9B4E9;
alter table nv_pair drop foreign key FK89B86AF1E979F767;
alter table org_entity drop foreign key FK11F8699E6F774771;
alter table org_entity drop foreign key FK11F8699EC4027D76;
alter table password_reset drop foreign key FKF4CE9AAB1D457434;
alter table password_reset drop foreign key FKF4CE9AAB666EC650;
alter table post drop foreign key FK3498A01D457434;
alter table post drop foreign key FK3498A03E5139F4;
alter table profile drop foreign key FKED8E89A96F774771;
alter table quiz_answer drop foreign key FKFD1EC0E8E07A0695;
alter table quiz_attempt drop foreign key FKB0FC76E342C9B4E9;
alter table repository drop foreign key FK7446DB4AE979F767;
alter table reward drop foreign key FKC84F4F2FC4027D76;
alter table reward_entry drop foreign key FK7B7C5B621FA8C924;
alter table reward_entry drop foreign key FK7B7C5B628FBEE285;
alter table reward_entry drop foreign key FK7B7C5B626015418A;
alter table signup_log drop foreign key FKE193F79DD360BEA0;
alter table signup_log drop foreign key FKE193F79DC4027D76;
alter table signup_log drop foreign key FKE193F79D1D457434;
alter table signup_log drop foreign key FKE193F79D666EC650;
alter table signup_log drop foreign key FKE193F79D5DA60797;
alter table sub_org drop foreign key FK90A57C65548BC540;
alter table sub_org drop foreign key FK90A57C6590036F4F;
alter table subordinate drop foreign key FK9448134C30DCD1AC;
alter table subordinate drop foreign key FK9448134C56958E49;
alter table website drop foreign key FK48F9E09B2CB36CA7;
alter table website drop foreign key FK48F9E09BC4027D76;
alter table website drop foreign key FK48F9E09B3A780E63;
drop table if exists access_log;
drop table if exists address_book;
drop table if exists alt_format;
drop table if exists app_control;
drop table if exists app_setting;
drop table if exists base_email_job;
drop table if exists base_entity;
drop table if exists base_process;
drop table if exists blob_hash;
drop table if exists branch;
drop table if exists cal_event;
drop table if exists cal_event_process;
drop table if exists calendar;
drop table if exists certificate;
drop table if exists comment;
drop table if exists commit_item;
drop table if exists contact;
drop table if exists contact_extended_property;
drop table if exists credential;
drop table if exists email_item;
drop table if exists email_send_attempt;
drop table if exists email_trigger;
drop table if exists enrolement;
drop table if exists fanout_entry;
drop table if exists fanout_hash;
drop table if exists forum;
drop table if exists forum_post;
drop table if exists forum_reply;
drop table if exists group_email_job;
drop table if exists group_entity;
drop table if exists group_in_website;
drop table if exists group_membership;
drop table if exists group_membership_application;
drop table if exists group_recipient;
drop table if exists group_reward;
drop table if exists group_role;
drop table if exists learner_process;
drop table if exists learning_log;
drop table if exists media_meta_data;
drop table if exists membership_process;
drop table if exists module_status;
drop table if exists module_status_field;
drop table if exists module_status_process;
drop table if exists named_counter;
drop table if exists nv_pair;
drop table if exists org_entity;
drop table if exists password_reset;
drop table if exists post;
drop table if exists profile;
drop table if exists quiz_answer;
drop table if exists quiz_attempt;
drop table if exists repository;
drop table if exists reward;
drop table if exists reward_entry;
drop table if exists signup_log;
drop table if exists sub_org;
drop table if exists subordinate;
drop table if exists version;
drop table if exists website;
create table access_log (id bigint not null auto_increment, content_type varchar(255), duration_ms bigint, num_bytes bigint, referrer longtext, req_date datetime not null, req_day integer not null, req_from varchar(255), req_host varchar(255), req_hour integer not null, req_method varchar(255) not null, req_month integer not null, req_user varchar(255), req_year integer not null, result_code integer not null, url varchar(255), organisation bigint, website bigint, primary key (id));
create table address_book (id bigint not null, primary key (id));
create table alt_format (id bigint not null auto_increment, alt_hash varchar(255) not null, name varchar(255) not null, source_hash varchar(255) not null, primary key (id), unique (name, source_hash));
create table app_control (id bigint not null auto_increment, enabled bit not null, modified_date datetime not null, name varchar(255) not null, modified_by bigint not null, organisation bigint, website_branch bigint, primary key (id), unique (name, organisation, website_branch));
create table app_setting (id bigint not null auto_increment, name varchar(255) not null, prop_value varchar(255), app_control bigint, primary key (id), unique (app_control, name));
create table base_email_job (id bigint not null auto_increment, from_address varchar(255), html varchar(255), name varchar(255) not null, notes varchar(255), subject varchar(255), title varchar(255), type varchar(255), organisation bigint not null, primary key (id));
create table base_entity (id bigint not null auto_increment, created_date datetime not null, modified_date datetime not null, name varchar(255) not null, notes varchar(255), type varchar(255), primary key (id), unique (name));
create table base_process (id bigint not null auto_increment, process_name varchar(255), process_version integer not null, state_name varchar(255), time_entered datetime, type varchar(255), primary key (id));
create table blob_hash (blob_hash bigint not null, volume_id bigint not null, primary key (blob_hash));
create table branch (id bigint not null auto_increment, created_date datetime, internal_theme varchar(255), name varchar(255), public_theme varchar(255), lockversion bigint, from_commit bigint, head bigint not null, repository bigint not null, primary key (id), unique (repository, name));
create table cal_event (id bigint not null auto_increment, created_date datetime not null, ctag bigint not null, description varchar(255), end_date datetime not null, modified_date datetime not null, name varchar(255) not null, start_date datetime not null, summary varchar(255), timezone varchar(255), calendar bigint not null, primary key (id));
create table cal_event_process (id bigint not null, cal_event bigint not null, primary key (id));
create table calendar (id bigint not null auto_increment, color varchar(255), created_date datetime not null, ctag bigint not null, modified_date datetime not null, name varchar(255) not null, owner bigint not null, primary key (id), unique (name, owner));
create table certificate (id bigint not null auto_increment, background_image_hash varchar(255), name varchar(255) not null, text varchar(255), title varchar(255) not null, organisation bigint not null, primary key (id), unique (name, organisation));
create table comment (content_href varchar(255) not null, content_id varchar(255) not null, content_title varchar(255) not null, id bigint not null, primary key (id));
create table commit_item (id bigint not null auto_increment, created_date datetime, item_hash varchar(255), previous_commit_id bigint, branch bigint, editor bigint not null, primary key (id));
create table contact (id bigint not null auto_increment, given_name varchar(255), mail varchar(255), name varchar(255) not null, organization_name varchar(255), sur_name varchar(255), telephonenumber varchar(255), uid varchar(255) not null, address_book bigint not null, primary key (id));
create table contact_extended_property (id bigint not null auto_increment, name varchar(255) not null, prop_value varchar(255) not null, contact bigint not null, primary key (id));
create table credential (type varchar(20) not null, id bigint not null auto_increment, created_date datetime not null, modified_date datetime not null, password varchar(255) not null, profile bigint not null, primary key (id));
create table email_item (id bigint not null auto_increment, bcc_list varchar(255), cc_list varchar(255), content_language varchar(255), created_date datetime not null, disposition varchar(255), encoding varchar(255), from_address varchar(255) not null, html longtext, message_size integer not null, next_attempt datetime, num_attempts integer, read_status bit not null, recipient_address varchar(255) not null, reply_to_address varchar(255) not null, send_status varchar(255), send_status_date datetime not null, subject varchar(255) not null, text longtext, to_list varchar(255), email_trigger bigint, job bigint, recipient bigint, sender bigint, primary key (id));
create table email_send_attempt (id bigint not null auto_increment, status varchar(255), status_date datetime not null, email_item bigint not null, primary key (id));
create table email_trigger (enabled bit not null, event_id varchar(255) not null, include_user bit not null, trigger_condition1 varchar(255), trigger_condition2 varchar(255), trigger_condition3 varchar(255), trigger_condition4 varchar(255), trigger_condition5 varchar(255), id bigint not null, primary key (id));
create table enrolement (id bigint not null auto_increment, completable bit, created_date date not null, enroled_course varchar(255), enroled_program varchar(255) not null, modified_date date not null, enroled_group bigint not null, website bigint not null, primary key (id));
create table fanout_entry (id bigint not null auto_increment, chunk_hash varchar(255), fanout bigint, primary key (id));
create table fanout_hash (id bigint not null auto_increment, actual_content_length bigint not null, fanout_hash varchar(255) not null, type varchar(255) not null, primary key (id), unique (type, fanout_hash));
create table forum (id bigint not null auto_increment, name varchar(255) not null, notes varchar(255), title varchar(255) not null, website bigint not null, primary key (id));
create table forum_post (name varchar(255) not null, title varchar(255) not null, id bigint not null, forum bigint not null, primary key (id));
create table forum_reply (id bigint not null, post bigint not null, primary key (id));
create table group_email_job (status varchar(255), status_date datetime not null, id bigint not null, primary key (id));
create table group_entity (id bigint not null auto_increment, created_date datetime not null, modified_date datetime not null, name varchar(255) not null, registration_mode varchar(255) not null, organisation bigint not null, primary key (id));
create table group_in_website (id bigint not null auto_increment, user_group bigint not null, website bigint not null, primary key (id), unique (user_group, website));
create table group_membership (id bigint not null auto_increment, created_date datetime not null, modified_date datetime not null, group_entity bigint not null, member bigint not null, within_org bigint not null, primary key (id), unique (member, group_entity, within_org));
create table group_membership_application (id bigint not null auto_increment, created_date datetime not null, modified_date datetime not null, admin_org bigint not null, group_entity bigint not null, member bigint not null, website bigint, within_org bigint not null, primary key (id));
create table group_recipient (id bigint not null auto_increment, job bigint not null, recipient bigint not null, primary key (id));
create table group_reward (id bigint not null auto_increment, created_date date, group_to_reward bigint not null, reward bigint not null, primary key (id), unique (group_to_reward, reward));
create table group_role (id bigint not null auto_increment, role_name varchar(255) not null, grantee bigint not null, repository bigint, within_org bigint, primary key (id), unique (role_name, grantee, within_org, repository));
create table learner_process (program_code varchar(255), id bigint not null, profile bigint not null, primary key (id));
create table learning_log (id bigint not null auto_increment, action varchar(255) not null, course_code varchar(255) not null, program_code varchar(255) not null, req_date datetime not null, req_day integer not null, req_hour integer not null, req_month integer not null, req_year integer not null, organisation bigint not null, profile bigint not null, website bigint, primary key (id));
create table media_meta_data (id bigint not null auto_increment, duration_secs integer, height integer, recorded_date datetime, source_hash varchar(255) not null, width integer, primary key (id), unique (source_hash));
create table membership_process (membership tinyblob, id bigint not null, primary key (id));
create table module_status (id bigint not null auto_increment, complete bit, course_code varchar(255) not null, created_date date not null, current_page varchar(255), modified_date date not null, module_code varchar(255) not null, percent_complete integer, program_code varchar(255) not null, profile bigint not null, website bigint not null, primary key (id), unique (website, program_code, course_code, module_code, profile));
create table module_status_field (id bigint not null auto_increment, mod_date date, name varchar(255) not null, prop_value varchar(255) not null, module_status bigint, primary key (id));
create table module_status_process (id bigint not null, module_status bigint not null, primary key (id));
create table named_counter (id bigint not null auto_increment, counter bigint not null, name varchar(255), primary key (id));
create table nv_pair (id bigint not null auto_increment, name varchar(255) not null, prop_value varchar(255) not null, base_entity bigint, primary key (id));
create table org_entity (deleted bit, org_id varchar(255) not null, title varchar(255), id bigint not null, organisation bigint, primary key (id), unique (org_id));
create table password_reset (id bigint not null auto_increment, created_date datetime not null, return_url varchar(255) not null, token varchar(255), profile bigint not null, website bigint, primary key (id), unique (token));
create table post (id bigint not null auto_increment, notes longtext, post_date datetime not null, poster bigint not null, website bigint not null, primary key (id));
create table profile (email varchar(255), enabled bit not null, first_name varchar(255), nick_name varchar(255), phone varchar(255), photo_hash varchar(255), rejected bit not null, sur_name varchar(255), id bigint not null, primary key (id));
create table quiz_answer (id bigint not null auto_increment, actual_answer varchar(255), answer_key varchar(255) not null, given_answer varchar(255) not null, quiz_attempt bigint, primary key (id));
create table quiz_attempt (id bigint not null auto_increment, created_date date not null, passed bit, module_status bigint not null, primary key (id));
create table repository (id bigint not null auto_increment, created_date datetime not null, deleted bit, live_branch varchar(255), name varchar(255) not null, notes varchar(255), public_content bit not null, title varchar(255), type varchar(255), base_entity bigint not null, primary key (id), unique (name, base_entity));
create table reward (id bigint not null auto_increment, background_image_hash varchar(255), body varchar(255), email_confirm bit, end_date date, fulfiled_date date, name varchar(255) not null, notes varchar(255), num_awarded integer, quiz_enabled bit not null, quiz_html varchar(255), start_date date, static_message varchar(255), static_message_enabled bit not null, status varchar(255), title varchar(255) not null, user_text_enabled bit not null, user_text_message varchar(255), user_upload_enabled bit not null, user_upload_message varchar(255), organisation bigint not null, primary key (id), unique (name, organisation));
create table reward_entry (id bigint not null auto_increment, awarded_date date not null, reason varchar(255) not null, awarded_for bigint, awarded_to bigint not null, reward bigint not null, primary key (id));
create table signup_log (id bigint not null auto_increment, req_date datetime not null, req_day integer not null, req_hour integer not null, req_month integer not null, req_year integer not null, group_entity bigint, membership_org bigint, organisation bigint, profile bigint, website bigint, primary key (id));
create table sub_org (id bigint not null auto_increment, owner bigint not null, suborg bigint not null, primary key (id));
create table subordinate (id bigint not null auto_increment, group_membership bigint not null, within_org bigint not null, primary key (id));
create table version (id bigint not null auto_increment, mod_date datetime not null, mod_hash varchar(255) not null, previous_mod_hash varchar(255) not null, profile_id bigint not null, resource_hash varchar(255) not null, primary key (id));
create table website (domain_name varchar(255), mail_server varchar(255), redirect_to varchar(255), id bigint not null, alias_to bigint, organisation bigint not null, primary key (id), unique (domain_name));
alter table access_log add index FKBBF335E9C4027D76 (organisation), add constraint FKBBF335E9C4027D76 foreign key (organisation) references org_entity (id);
alter table access_log add index FKBBF335E91D457434 (website), add constraint FKBBF335E91D457434 foreign key (website) references website (id);
alter table address_book add index FKFB8B0542CB36CA7 (id), add constraint FKFB8B0542CB36CA7 foreign key (id) references repository (id);
alter table app_control add index FKEE00D53FC4027D76 (organisation), add constraint FKEE00D53FC4027D76 foreign key (organisation) references org_entity (id);
alter table app_control add index FKEE00D53F1CD5CA34 (modified_by), add constraint FKEE00D53F1CD5CA34 foreign key (modified_by) references profile (id);
alter table app_control add index FKEE00D53F849B88AA (website_branch), add constraint FKEE00D53F849B88AA foreign key (website_branch) references branch (id);
alter table app_setting add index FK2BA87E323BD6907C (app_control), add constraint FK2BA87E323BD6907C foreign key (app_control) references app_control (id);
alter table base_email_job add index FK2F3D58ACC4027D76 (organisation), add constraint FK2F3D58ACC4027D76 foreign key (organisation) references org_entity (id);
create index ids_entity_name on base_entity (name);
create index ids_blobhash on blob_hash (blob_hash);
alter table branch add index FKADAF25A2EDBCEC59 (head), add constraint FKADAF25A2EDBCEC59 foreign key (head) references commit_item (id);
alter table branch add index FKADAF25A2A0FA3AD6 (repository), add constraint FKADAF25A2A0FA3AD6 foreign key (repository) references repository (id);
alter table branch add index FKADAF25A2DE9B19E5 (from_commit), add constraint FKADAF25A2DE9B19E5 foreign key (from_commit) references commit_item (id);
alter table cal_event add index FKABCCC2E9C9A2A33E (calendar), add constraint FKABCCC2E9C9A2A33E foreign key (calendar) references calendar (id);
alter table cal_event_process add index FK68508F19CBDEA377 (id), add constraint FK68508F19CBDEA377 foreign key (id) references base_process (id);
alter table cal_event_process add index FK68508F197E512317 (cal_event), add constraint FK68508F197E512317 foreign key (cal_event) references cal_event (id);
alter table calendar add index FKF55EFB3E75CB2D09 (owner), add constraint FKF55EFB3E75CB2D09 foreign key (owner) references base_entity (id);
alter table certificate add index FK745F4197C4027D76 (organisation), add constraint FK745F4197C4027D76 foreign key (organisation) references org_entity (id);
alter table comment add index FK38A5EE5F889319BC (id), add constraint FK38A5EE5F889319BC foreign key (id) references post (id);
alter table commit_item add index FK5A19351B2AEC5E14 (editor), add constraint FK5A19351B2AEC5E14 foreign key (editor) references profile (id);
alter table commit_item add index FK5A19351B99AB4086 (branch), add constraint FK5A19351B99AB4086 foreign key (branch) references branch (id);
alter table contact add index FK38B7242012BCE54F (address_book), add constraint FK38B7242012BCE54F foreign key (address_book) references address_book (id);
alter table contact_extended_property add index FK8C45B7BCFCBFFB3E (contact), add constraint FK8C45B7BCFCBFFB3E foreign key (contact) references contact (id);
alter table credential add index FKD743E857666EC650 (profile), add constraint FKD743E857666EC650 foreign key (profile) references profile (id);
alter table email_item add index FK4E94C9D642E056DC (sender), add constraint FK4E94C9D642E056DC foreign key (sender) references profile (id);
alter table email_item add index FK4E94C9D6A058AC6F (recipient), add constraint FK4E94C9D6A058AC6F foreign key (recipient) references base_entity (id);
alter table email_item add index FK4E94C9D67DB36992 (email_trigger), add constraint FK4E94C9D67DB36992 foreign key (email_trigger) references email_trigger (id);
alter table email_item add index FK4E94C9D6AACF6B30 (job), add constraint FK4E94C9D6AACF6B30 foreign key (job) references base_email_job (id);
alter table email_send_attempt add index FK7EF81CD9C331BB44 (email_item), add constraint FK7EF81CD9C331BB44 foreign key (email_item) references email_item (id);
alter table email_trigger add index FKD75DEC15AACDDC8E (id), add constraint FKD75DEC15AACDDC8E foreign key (id) references base_email_job (id);
alter table enrolement add index FK688BE7D1D457434 (website), add constraint FK688BE7D1D457434 foreign key (website) references website (id);
alter table enrolement add index FK688BE7D73B79E62 (enroled_group), add constraint FK688BE7D73B79E62 foreign key (enroled_group) references group_entity (id);
create index ids_chunk_hash on fanout_entry (chunk_hash);
alter table fanout_entry add index FKD6C6228E4FF762A6 (fanout), add constraint FKD6C6228E4FF762A6 foreign key (fanout) references fanout_hash (id);
alter table forum add index FK5D18D211D457434 (website), add constraint FK5D18D211D457434 foreign key (website) references website (id);
alter table forum_post add index FKCDDDD0FE889319BC (id), add constraint FKCDDDD0FE889319BC foreign key (id) references post (id);
alter table forum_post add index FKCDDDD0FE8F132661 (forum), add constraint FKCDDDD0FE8F132661 foreign key (forum) references forum (id);
alter table forum_reply add index FKEDF3E64C889319BC (id), add constraint FKEDF3E64C889319BC foreign key (id) references post (id);
alter table forum_reply add index FKEDF3E64C70AD9EA0 (post), add constraint FKEDF3E64C70AD9EA0 foreign key (post) references forum_post (id);
alter table group_email_job add index FKF4C306DAAACDDC8E (id), add constraint FKF4C306DAAACDDC8E foreign key (id) references base_email_job (id);
alter table group_entity add index FKDC016DA3C4027D76 (organisation), add constraint FKDC016DA3C4027D76 foreign key (organisation) references org_entity (id);
alter table group_in_website add index FK121949416A085208 (user_group), add constraint FK121949416A085208 foreign key (user_group) references group_entity (id);
alter table group_in_website add index FK121949411D457434 (website), add constraint FK121949411D457434 foreign key (website) references website (id);
alter table group_membership add index FK526319638A2C741 (member), add constraint FK526319638A2C741 foreign key (member) references profile (id);
alter table group_membership add index FK5263196D360BEA0 (group_entity), add constraint FK5263196D360BEA0 foreign key (group_entity) references group_entity (id);
alter table group_membership add index FK526319630DCD1AC (within_org), add constraint FK526319630DCD1AC foreign key (within_org) references org_entity (id);
alter table group_membership_application add index FK88BAE42738A2C741 (member), add constraint FK88BAE42738A2C741 foreign key (member) references profile (id);
alter table group_membership_application add index FK88BAE427D360BEA0 (group_entity), add constraint FK88BAE427D360BEA0 foreign key (group_entity) references group_entity (id);
alter table group_membership_application add index FK88BAE42730DCD1AC (within_org), add constraint FK88BAE42730DCD1AC foreign key (within_org) references org_entity (id);
alter table group_membership_application add index FK88BAE4271D457434 (website), add constraint FK88BAE4271D457434 foreign key (website) references website (id);
alter table group_membership_application add index FK88BAE4278B021E90 (admin_org), add constraint FK88BAE4278B021E90 foreign key (admin_org) references org_entity (id);
alter table group_recipient add index FKA73C90592840C316 (recipient), add constraint FKA73C90592840C316 foreign key (recipient) references group_entity (id);
alter table group_recipient add index FKA73C9059AACF6B30 (job), add constraint FKA73C9059AACF6B30 foreign key (job) references base_email_job (id);
alter table group_reward add index FKF1B2D8EF1FA8C924 (reward), add constraint FKF1B2D8EF1FA8C924 foreign key (reward) references reward (id);
alter table group_reward add index FKF1B2D8EF111E7CB0 (group_to_reward), add constraint FKF1B2D8EF111E7CB0 foreign key (group_to_reward) references group_entity (id);
alter table group_role add index FK4C707A3630DCD1AC (within_org), add constraint FK4C707A3630DCD1AC foreign key (within_org) references org_entity (id);
alter table group_role add index FK4C707A36A0FA3AD6 (repository), add constraint FK4C707A36A0FA3AD6 foreign key (repository) references repository (id);
alter table group_role add index FK4C707A3681447B9 (grantee), add constraint FK4C707A3681447B9 foreign key (grantee) references group_entity (id);
alter table learner_process add index FKADA71E21CBDEA377 (id), add constraint FKADA71E21CBDEA377 foreign key (id) references base_process (id);
alter table learner_process add index FKADA71E21666EC650 (profile), add constraint FKADA71E21666EC650 foreign key (profile) references profile (id);
alter table learning_log add index FKAA3686A3C4027D76 (organisation), add constraint FKAA3686A3C4027D76 foreign key (organisation) references org_entity (id);
alter table learning_log add index FKAA3686A31D457434 (website), add constraint FKAA3686A31D457434 foreign key (website) references website (id);
alter table learning_log add index FKAA3686A3666EC650 (profile), add constraint FKAA3686A3666EC650 foreign key (profile) references profile (id);
alter table membership_process add index FKC4F81706CBDEA377 (id), add constraint FKC4F81706CBDEA377 foreign key (id) references base_process (id);
alter table module_status add index FKA7AA55251D457434 (website), add constraint FKA7AA55251D457434 foreign key (website) references website (id);
alter table module_status add index FKA7AA5525666EC650 (profile), add constraint FKA7AA5525666EC650 foreign key (profile) references profile (id);
alter table module_status_field add index FK7F08296042C9B4E9 (module_status), add constraint FK7F08296042C9B4E9 foreign key (module_status) references module_status (id);
alter table module_status_process add index FKFE862555CBDEA377 (id), add constraint FKFE862555CBDEA377 foreign key (id) references base_process (id);
alter table module_status_process add index FKFE86255542C9B4E9 (module_status), add constraint FKFE86255542C9B4E9 foreign key (module_status) references module_status (id);
alter table nv_pair add index FK89B86AF1E979F767 (base_entity), add constraint FK89B86AF1E979F767 foreign key (base_entity) references base_entity (id);
create index idx_orgId on org_entity (org_id);
alter table org_entity add index FK11F8699E6F774771 (id), add constraint FK11F8699E6F774771 foreign key (id) references base_entity (id);
alter table org_entity add index FK11F8699EC4027D76 (organisation), add constraint FK11F8699EC4027D76 foreign key (organisation) references org_entity (id);
alter table password_reset add index FKF4CE9AAB1D457434 (website), add constraint FKF4CE9AAB1D457434 foreign key (website) references website (id);
alter table password_reset add index FKF4CE9AAB666EC650 (profile), add constraint FKF4CE9AAB666EC650 foreign key (profile) references profile (id);
alter table post add index FK3498A01D457434 (website), add constraint FK3498A01D457434 foreign key (website) references website (id);
alter table post add index FK3498A03E5139F4 (poster), add constraint FK3498A03E5139F4 foreign key (poster) references profile (id);
create index ids_profile_email on profile (email);
alter table profile add index FKED8E89A96F774771 (id), add constraint FKED8E89A96F774771 foreign key (id) references base_entity (id);
alter table quiz_answer add index FKFD1EC0E8E07A0695 (quiz_attempt), add constraint FKFD1EC0E8E07A0695 foreign key (quiz_attempt) references quiz_attempt (id);
alter table quiz_attempt add index FKB0FC76E342C9B4E9 (module_status), add constraint FKB0FC76E342C9B4E9 foreign key (module_status) references module_status (id);
alter table repository add index FK7446DB4AE979F767 (base_entity), add constraint FK7446DB4AE979F767 foreign key (base_entity) references base_entity (id);
alter table reward add index FKC84F4F2FC4027D76 (organisation), add constraint FKC84F4F2FC4027D76 foreign key (organisation) references org_entity (id);
alter table reward_entry add index FK7B7C5B621FA8C924 (reward), add constraint FK7B7C5B621FA8C924 foreign key (reward) references reward (id);
alter table reward_entry add index FK7B7C5B628FBEE285 (awarded_to), add constraint FK7B7C5B628FBEE285 foreign key (awarded_to) references profile (id);
alter table reward_entry add index FK7B7C5B626015418A (awarded_for), add constraint FK7B7C5B626015418A foreign key (awarded_for) references module_status (id);
alter table signup_log add index FKE193F79DD360BEA0 (group_entity), add constraint FKE193F79DD360BEA0 foreign key (group_entity) references group_entity (id);
alter table signup_log add index FKE193F79DC4027D76 (organisation), add constraint FKE193F79DC4027D76 foreign key (organisation) references org_entity (id);
alter table signup_log add index FKE193F79D1D457434 (website), add constraint FKE193F79D1D457434 foreign key (website) references website (id);
alter table signup_log add index FKE193F79D666EC650 (profile), add constraint FKE193F79D666EC650 foreign key (profile) references profile (id);
alter table signup_log add index FKE193F79D5DA60797 (membership_org), add constraint FKE193F79D5DA60797 foreign key (membership_org) references org_entity (id);
alter table sub_org add index FK90A57C65548BC540 (suborg), add constraint FK90A57C65548BC540 foreign key (suborg) references org_entity (id);
alter table sub_org add index FK90A57C6590036F4F (owner), add constraint FK90A57C6590036F4F foreign key (owner) references org_entity (id);
alter table subordinate add index FK9448134C30DCD1AC (within_org), add constraint FK9448134C30DCD1AC foreign key (within_org) references org_entity (id);
alter table subordinate add index FK9448134C56958E49 (group_membership), add constraint FK9448134C56958E49 foreign key (group_membership) references group_membership (id);
create index idx_version_modhash on version (mod_hash);
create index idx_domain_name on website (domain_name);
alter table website add index FK48F9E09B2CB36CA7 (id), add constraint FK48F9E09B2CB36CA7 foreign key (id) references repository (id);
alter table website add index FK48F9E09BC4027D76 (organisation), add constraint FK48F9E09BC4027D76 foreign key (organisation) references org_entity (id);
alter table website add index FK48F9E09B3A780E63 (alias_to), add constraint FK48F9E09B3A780E63 foreign key (alias_to) references website (id);
