--INCF DB Schema (Selected Tables)
--Extracted from Postgres DB name: atlas

CREATE TABLE "public"."hub"
(
   hub_id numeric(19) NOT NULL,
   hub_code text PRIMARY KEY NOT NULL,
   hub_title text NOT NULL,
   hub_description text,
   hub_capabilities_url text,
   hub_registered_date date,
   hub_last_update date,
   hub_status_code text,
   hub_checkfrequency_days numeric(19),
   hub_website text,
   contact_name text,
   contact_email text,
   organization text,
   logo_icon_path text,
   citations text
)
;
CREATE UNIQUE INDEX pk_hub ON hub(hub_code)
;
CREATE TABLE "public"."hubservice"
(
   hub_service_id numeric(19) NOT NULL,
   hub_service_code text PRIMARY KEY NOT NULL,
   hub_service_type text NOT NULL,
   hub_service_capabilities_url text NOT NULL,
   hub_service_update_frequency text,
   contact_name text,
   contact_email text,
   hub_service_name text NOT NULL,
   hub_service_version text,
   hub_code text NOT NULL
)
;
ALTER TABLE hubservice
ADD CONSTRAINT fk_hubservice_hub
FOREIGN KEY (hub_code)
REFERENCES hub(hub_code) ON DELETE NO ACTION ON UPDATE NO ACTION

;
CREATE UNIQUE INDEX pk_hubservice ON hubservice(hub_service_code)
;
CREATE TABLE "public"."process"
(
   process_id numeric(19) NOT NULL,
   process_code text PRIMARY KEY NOT NULL,
   process_name text NOT NULL,
   process_version text,
   process_description text,
   implementing_hub_service_code text NOT NULL,
   process_algorithm text,
   process_uri text,
   process_citation text,
   process_call_url text NOT NULL
)
;
ALTER TABLE process
ADD CONSTRAINT fk_process_hubservice
FOREIGN KEY (implementing_hub_service_code)
REFERENCES hubservice(hub_service_code) ON DELETE NO ACTION ON UPDATE NO ACTION

;
CREATE UNIQUE INDEX pk_process ON process(process_code)
;
CREATE TABLE "public"."processparameter"
(
   process_parameter_id numeric(19) NOT NULL,
   process_parameter_code text PRIMARY KEY NOT NULL,
   process_parameter_name text NOT NULL,
   process_code text NOT NULL,
   process_parameter_type text,
   process_parameter_units_abbreviation text,
   process_parameter_units_name text,
   process_parameter_description text
)
;
ALTER TABLE processparameter
ADD CONSTRAINT fk_processparameter_process
FOREIGN KEY (process_code)
REFERENCES process(process_code) ON DELETE NO ACTION ON UPDATE NO ACTION

;
CREATE UNIQUE INDEX pk_processparameter ON processparameter(process_parameter_code)
;
CREATE TABLE "public"."project"
(
   project_id numeric(19) NOT NULL,
   project_code text PRIMARY KEY NOT NULL,
   project_name text,
   project_description text
)
;
CREATE UNIQUE INDEX pk_project ON project(project_code)
;
