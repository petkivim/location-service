--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- Name: location_service; Type: DATABASE; Schema: -; Owner: -
--

REVOKE ALL PRIVILEGES ON DATABASE location_service FROM location_service_login;
REVOKE ALL PRIVILEGES ON SCHEMA public FROM location_service_login;

REVOKE ALL PRIVILEGES ON DATABASE location_service FROM location_service_admin;
REVOKE ALL PRIVILEGES ON SCHEMA public FROM location_service_admin;

REVOKE ALL PRIVILEGES ON DATABASE location_service FROM location_service;
REVOKE ALL PRIVILEGES ON SCHEMA public FROM location_service;

DROP DATABASE IF EXISTS location_service;

DROP role IF EXISTS location_service_login;
DROP role IF EXISTS location_service_admin;
DROP role IF EXISTS location_service;

CREATE DATABASE location_service WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'fi_FI.utf8' LC_CTYPE = 'fi_FI.utf8';

\connect location_service

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: -
--

CREATE OR REPLACE PROCEDURAL LANGUAGE plpgsql;


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: area; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE area (
    area_id integer NOT NULL,
    x1 integer,
    y1 integer,
    x2 integer,
    y2 integer,
    location_id integer,
    angle integer
);


--
-- Name: collection; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE collection (
    location_id integer NOT NULL,
    location_code character varying(100),
    map_id integer,
    image_id integer,
    owner_id integer NOT NULL,
    name character varying(100),
    floor character varying(100),
    staff_note_1 character varying(2000),
    staff_note_2 character varying(2000),
    created timestamp without time zone,
    creator character varying(100),
    updated timestamp without time zone,
    updater character varying(100),
    library_id integer,
    is_substring boolean,
    collection_code character varying(50),
    shelf_number character varying(40)
);


--
-- Name: description; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE description (
    description_id integer NOT NULL,
    description character varying(1000),
    language_id integer,
    location_id integer
);


--
-- Name: image; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE image (
    id integer NOT NULL,
    path character varying(500),
    description character varying(100),
    isexternal character(1),
    owner_id integer NOT NULL,
    created timestamp without time zone,
    creator character varying(100),
    updated timestamp without time zone,
    updater character varying(100),
    x integer,
    y integer
);


--
-- Name: language; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE language (
    id integer NOT NULL,
    code character varying(20),
    name character varying(50),
    owner_id integer NOT NULL,
    created timestamp without time zone,
    creator character varying(100),
    updated timestamp without time zone,
    updater character varying(100)
);


--
-- Name: library; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE library (
    location_id integer NOT NULL,
    location_code character varying(100),
    map_id integer,
    image_id integer,
    owner_id integer NOT NULL,
    name character varying(100),
    floor character varying(100),
    staff_note_1 character varying(2000),
    staff_note_2 character varying(2000),
    created timestamp without time zone,
    creator character varying(100),
    updated timestamp without time zone,
    updater character varying(100)
);


--
-- Name: map; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE map (
    id integer NOT NULL,
    path character varying(500),
    description character varying(100),
    isexternal character(1),
    owner_id integer NOT NULL,
    created timestamp without time zone,
    creator character varying(100),
    updated timestamp without time zone,
    updater character varying(100),
    color character varying(6),
    opacity character varying(3)
);


--
-- Name: my_user; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE my_user (
    login character varying(15) NOT NULL,
    first_name character varying(100),
    last_name character varying(100),
    pass character varying(100),
    email character varying(100),
    organization character varying(100),
    owner_id integer NOT NULL,
    created timestamp without time zone,
    creator character varying(100),
    updated timestamp without time zone,
    updater character varying(100)
);


--
-- Name: not_found_redirect; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE not_found_redirect (
    id integer NOT NULL,
    mod_condition character varying(200),
    mod_operation character varying(200),
    is_active boolean,
    owner_id integer
);


--
-- Name: note; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE note (
    note_id integer NOT NULL,
    note character varying(2000),
    language_id integer,
    location_id integer
);


--
-- Name: owner; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE owner (
    id integer NOT NULL,
    code character varying(10),
    name character varying(100),
    color character varying(6),
    opacity character varying(3),
    exporter_visible boolean,
    allowed_ips character varying(500),
    created timestamp without time zone,
    creator character varying(100),
    updated timestamp without time zone,
    updater character varying(100),
    locating_strategy character varying(255) NOT NULL
);


--
-- Name: preprocessing_redirect; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE preprocessing_redirect (
    id integer NOT NULL,
    mod_condition character varying(200),
    mod_operation character varying(200),
    is_active boolean,
    owner_id integer
);


--
-- Name: search_event; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE search_event (
    id integer NOT NULL,
    call_number character varying(300),
    collection_code character varying(100),
    language_code character varying(100),
    status character varying(100),
    search_type character varying(100),
    "position" character varying(100),
    is_authorized boolean,
    ip_address character varying(100),
    event_type character varying(255) NOT NULL,
    processing_time bigint,
    owner_code character varying(10),
    event_date timestamp without time zone
);


--
-- Name: search_index; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE search_index (
    id integer NOT NULL,
    location_id integer NOT NULL,
    location_type character varying(255) NOT NULL,
    call_number character varying(300) NOT NULL,
    location_code character varying(100) NOT NULL,
    collection_code character varying(50),
    owner_id integer NOT NULL
);


--
-- Name: shelf; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE shelf (
    location_id integer NOT NULL,
    location_code character varying(100),
    map_id integer,
    image_id integer,
    owner_id integer NOT NULL,
    name character varying(100),
    floor character varying(100),
    staff_note_1 character varying(2000),
    staff_note_2 character varying(2000),
    created timestamp without time zone,
    creator character varying(100),
    updated timestamp without time zone,
    updater character varying(100),
    collection_id integer,
    shelf_number character varying(40)
);


--
-- Name: subject_matter; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE subject_matter (
    subject_matter_id integer NOT NULL,
    index_term character varying(100),
    language_id integer,
    owner_id integer NOT NULL,
    created timestamp without time zone,
    creator character varying(100),
    updated timestamp without time zone,
    updater character varying(100)
);


--
-- Name: subject_matters; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE subject_matters (
    location_id integer NOT NULL,
    subject_matter_id integer NOT NULL
);


--
-- Name: user_info; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE user_info (
    user_info_id integer NOT NULL,
    login character varying(15) NOT NULL,
    group_name character varying(15) NOT NULL
);


--
-- Data for Name: area; Type: TABLE DATA; Schema: public; Owner: -
--

COPY area (area_id, x1, y1, x2, y2, location_id, angle) FROM stdin;
\.


--
-- Data for Name: collection; Type: TABLE DATA; Schema: public; Owner: -
--

COPY collection (location_id, location_code, map_id, image_id, owner_id, name, floor, staff_note_1, staff_note_2, created, creator, updated, updater, library_id, is_substring, collection_code, shelf_number) FROM stdin;
\.


--
-- Data for Name: description; Type: TABLE DATA; Schema: public; Owner: -
--

COPY description (description_id, description, language_id, location_id) FROM stdin;
\.


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: -
--

COPY image (id, path, description, isexternal, owner_id, created, creator, updated, updater, x, y) FROM stdin;
\.


--
-- Data for Name: language; Type: TABLE DATA; Schema: public; Owner: -
--

COPY language (id, code, name, owner_id, created, creator, updated, updater) FROM stdin;
\.


--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: -
--

COPY library (location_id, location_code, map_id, image_id, owner_id, name, floor, staff_note_1, staff_note_2, created, creator, updated, updater) FROM stdin;
\.


--
-- Data for Name: map; Type: TABLE DATA; Schema: public; Owner: -
--

COPY map (id, path, description, isexternal, owner_id, created, creator, updated, updater, color, opacity) FROM stdin;
\.


--
-- Data for Name: my_user; Type: TABLE DATA; Schema: public; Owner: -
--

COPY my_user (login, first_name, last_name, pass, email, organization, owner_id, created, creator, updated, updater) FROM stdin;
admin	Admin	Admin	13d8c9009647abffee45341993ab3952f57c361d	admin@example.com	Organization	1	2014-06-23 10:45:33.428545	SYSTEM	\N	\N
\.


--
-- Data for Name: not_found_redirect; Type: TABLE DATA; Schema: public; Owner: -
--

COPY not_found_redirect (id, mod_condition, mod_operation, is_active, owner_id) FROM stdin;
\.


--
-- Data for Name: note; Type: TABLE DATA; Schema: public; Owner: -
--

COPY note (note_id, note, language_id, location_id) FROM stdin;
\.


--
-- Data for Name: owner; Type: TABLE DATA; Schema: public; Owner: -
--

COPY owner (id, code, name, color, opacity, exporter_visible, allowed_ips, created, creator, updated, updater, locating_strategy) FROM stdin;
1	ADMIN	Admin	dd0000	150	f		2014-06-23 10:40:12.72846	SYSTEM	\N	\N	INDEX_EXTERNAL
\.


--
-- Data for Name: preprocessing_redirect; Type: TABLE DATA; Schema: public; Owner: -
--

COPY preprocessing_redirect (id, mod_condition, mod_operation, is_active, owner_id) FROM stdin;
\.


--
-- Data for Name: search_event; Type: TABLE DATA; Schema: public; Owner: -
--

COPY search_event (id, call_number, collection_code, language_code, status, search_type, "position", is_authorized, ip_address, event_type, processing_time, owner_code, event_date) FROM stdin;
\.


--
-- Data for Name: search_index; Type: TABLE DATA; Schema: public; Owner: -
--

COPY search_index (id, location_id, location_type, call_number, location_code, collection_code, owner_id) FROM stdin;
\.


--
-- Data for Name: shelf; Type: TABLE DATA; Schema: public; Owner: -
--

COPY shelf (location_id, location_code, map_id, image_id, owner_id, name, floor, staff_note_1, staff_note_2, created, creator, updated, updater, collection_id, shelf_number) FROM stdin;
\.


--
-- Data for Name: subject_matter; Type: TABLE DATA; Schema: public; Owner: -
--

COPY subject_matter (subject_matter_id, index_term, language_id, owner_id, created, creator, updated, updater) FROM stdin;
\.


--
-- Data for Name: subject_matters; Type: TABLE DATA; Schema: public; Owner: -
--

COPY subject_matters (location_id, subject_matter_id) FROM stdin;
\.


--
-- Data for Name: user_info; Type: TABLE DATA; Schema: public; Owner: -
--

COPY user_info (user_info_id, login, group_name) FROM stdin;
1	admin	ADMIN
\.


--
-- Name: area_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY area
    ADD CONSTRAINT area_pkey PRIMARY KEY (area_id);


--
-- Name: collection_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY collection
    ADD CONSTRAINT collection_pkey PRIMARY KEY (location_id);


--
-- Name: description_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY description
    ADD CONSTRAINT description_pkey PRIMARY KEY (description_id);


--
-- Name: image_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY image
    ADD CONSTRAINT image_pkey PRIMARY KEY (id);


--
-- Name: language_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY language
    ADD CONSTRAINT language_pkey PRIMARY KEY (id);


--
-- Name: library_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY library
    ADD CONSTRAINT library_pkey PRIMARY KEY (location_id);


--
-- Name: map_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY map
    ADD CONSTRAINT map_pkey PRIMARY KEY (id);


--
-- Name: my_user_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY my_user
    ADD CONSTRAINT my_user_pkey PRIMARY KEY (login);


--
-- Name: not_found_redirect_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY not_found_redirect
    ADD CONSTRAINT not_found_redirect_pkey PRIMARY KEY (id);


--
-- Name: note_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY note
    ADD CONSTRAINT note_pkey PRIMARY KEY (note_id);


--
-- Name: owner_code_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY owner
    ADD CONSTRAINT owner_code_key UNIQUE (code);


--
-- Name: owner_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY owner
    ADD CONSTRAINT owner_pkey PRIMARY KEY (id);


--
-- Name: preprocessing_redirect_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY preprocessing_redirect
    ADD CONSTRAINT preprocessing_redirect_pkey PRIMARY KEY (id);


--
-- Name: search_event_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY search_event
    ADD CONSTRAINT search_event_pkey PRIMARY KEY (id);


--
-- Name: search_index_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY search_index
    ADD CONSTRAINT search_index_pkey PRIMARY KEY (id);


--
-- Name: shelf_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY shelf
    ADD CONSTRAINT shelf_pkey PRIMARY KEY (location_id);


--
-- Name: subject_matter_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY subject_matter
    ADD CONSTRAINT subject_matter_pkey PRIMARY KEY (subject_matter_id);


--
-- Name: user_info_login_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY user_info
    ADD CONSTRAINT user_info_login_key UNIQUE (login);


--
-- Name: user_info_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY user_info
    ADD CONSTRAINT user_info_pkey PRIMARY KEY (user_info_id);


--
-- Name: _location_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX _location_id_idx ON search_index USING btree (location_id);


--
-- Name: _location_type_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX _location_type_idx ON search_index USING btree (location_type);


--
-- Name: area_location_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX area_location_id_idx ON area USING btree (location_id);


--
-- Name: code_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX code_idx ON language USING btree (code);


--
-- Name: collection_code_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX collection_code_idx ON collection USING btree (collection_code);


--
-- Name: collection_library_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX collection_library_id_idx ON collection USING btree (library_id);


--
-- Name: collection_location_code_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX collection_location_code_idx ON collection USING btree (location_code);


--
-- Name: collection_owner_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX collection_owner_id_idx ON collection USING btree (owner_id);


--
-- Name: description_language_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX description_language_id_idx ON description USING btree (language_id);


--
-- Name: description_location_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX description_location_id_idx ON description USING btree (location_id);


--
-- Name: image_owner_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX image_owner_id_idx ON image USING btree (owner_id);


--
-- Name: index_term_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_term_idx ON subject_matter USING btree (index_term);


--
-- Name: language_owner_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX language_owner_id_idx ON language USING btree (owner_id);


--
-- Name: library_location_code_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX library_location_code_idx ON library USING btree (location_code);


--
-- Name: library_owner_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX library_owner_id_idx ON library USING btree (owner_id);


--
-- Name: location_code_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX location_code_idx ON search_index USING btree (location_code);


--
-- Name: map_owner_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX map_owner_id_idx ON map USING btree (owner_id);


--
-- Name: my_user_owner_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX my_user_owner_id_idx ON my_user USING btree (owner_id);


--
-- Name: not_found_redirect_owner_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX not_found_redirect_owner_id_idx ON not_found_redirect USING btree (owner_id);


--
-- Name: note_language_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX note_language_id_idx ON note USING btree (language_id);


--
-- Name: note_location_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX note_location_id_idx ON note USING btree (location_id);


--
-- Name: preprocessing_redirect_owner_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX preprocessing_redirect_owner_id_idx ON preprocessing_redirect USING btree (owner_id);


--
-- Name: search_event_owner_code_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX search_event_owner_code_idx ON search_event USING btree (owner_code);


--
-- Name: shelf_collection_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX shelf_collection_id_idx ON shelf USING btree (collection_id);


--
-- Name: shelf_location_code_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX shelf_location_code_idx ON shelf USING btree (location_code);


--
-- Name: shelf_owner_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX shelf_owner_id_idx ON shelf USING btree (owner_id);


--
-- Name: subject_matter_owner_id_idx; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX subject_matter_owner_id_idx ON subject_matter USING btree (owner_id);


--
-- Name: fk198917dc64469a5c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY description
    ADD CONSTRAINT fk198917dc64469a5c FOREIGN KEY (language_id) REFERENCES language(id);


--
-- Name: fk24a7f264469a5c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY note
    ADD CONSTRAINT fk24a7f264469a5c FOREIGN KEY (language_id) REFERENCES language(id);


--
-- Name: fk3ef5b97098677c781293c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY map
    ADD CONSTRAINT fk3ef5b97098677c781293c FOREIGN KEY (owner_id) REFERENCES owner(id);


--
-- Name: fk3ef5b97098677c78428b13b; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY image
    ADD CONSTRAINT fk3ef5b97098677c78428b13b FOREIGN KEY (owner_id) REFERENCES owner(id);


--
-- Name: fk4b3660a5b7586b3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY shelf
    ADD CONSTRAINT fk4b3660a5b7586b3 FOREIGN KEY (collection_id) REFERENCES collection(location_id);


--
-- Name: fk5679a95464469a5c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY subject_matter
    ADD CONSTRAINT fk5679a95464469a5c FOREIGN KEY (language_id) REFERENCES language(id);


--
-- Name: fk5679a95498677c78; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY subject_matter
    ADD CONSTRAINT fk5679a95498677c78 FOREIGN KEY (owner_id) REFERENCES owner(id);


--
-- Name: fk57304b2598677c78; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY not_found_redirect
    ADD CONSTRAINT fk57304b2598677c78 FOREIGN KEY (owner_id) REFERENCES owner(id);


--
-- Name: fk752a03d51b01b7834b3b09b; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY library
    ADD CONSTRAINT fk752a03d51b01b7834b3b09b FOREIGN KEY (image_id) REFERENCES image(id);


--
-- Name: fk752a03d51b01b784b3660a; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY shelf
    ADD CONSTRAINT fk752a03d51b01b784b3660a FOREIGN KEY (image_id) REFERENCES image(id);


--
-- Name: fk752a03d51b01b78b51f9a9e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY collection
    ADD CONSTRAINT fk752a03d51b01b78b51f9a9e FOREIGN KEY (image_id) REFERENCES image(id);


--
-- Name: fk752a03d562a15f5834b3b09b; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY library
    ADD CONSTRAINT fk752a03d562a15f5834b3b09b FOREIGN KEY (map_id) REFERENCES map(id);


--
-- Name: fk752a03d562a15f584b3660a; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY shelf
    ADD CONSTRAINT fk752a03d562a15f584b3660a FOREIGN KEY (map_id) REFERENCES map(id);


--
-- Name: fk752a03d562a15f58b51f9a9e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY collection
    ADD CONSTRAINT fk752a03d562a15f58b51f9a9e FOREIGN KEY (map_id) REFERENCES map(id);


--
-- Name: fk752a03d598677c7834b3b09b; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY library
    ADD CONSTRAINT fk752a03d598677c7834b3b09b FOREIGN KEY (owner_id) REFERENCES owner(id);


--
-- Name: fk752a03d598677c784b3660a; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY shelf
    ADD CONSTRAINT fk752a03d598677c784b3660a FOREIGN KEY (owner_id) REFERENCES owner(id);


--
-- Name: fk752a03d598677c78b51f9a9e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY collection
    ADD CONSTRAINT fk752a03d598677c78b51f9a9e FOREIGN KEY (owner_id) REFERENCES owner(id);


--
-- Name: fk78bb817fb10154b1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY subject_matters
    ADD CONSTRAINT fk78bb817fb10154b1 FOREIGN KEY (subject_matter_id) REFERENCES subject_matter(subject_matter_id);


--
-- Name: fk8681be7e98677c78; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY my_user
    ADD CONSTRAINT fk8681be7e98677c78 FOREIGN KEY (owner_id) REFERENCES owner(id);


--
-- Name: fkb51f9a9e21d96ff8; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY collection
    ADD CONSTRAINT fkb51f9a9e21d96ff8 FOREIGN KEY (library_id) REFERENCES library(location_id);


--
-- Name: fkbc12d8a2c79c05; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_info
    ADD CONSTRAINT fkbc12d8a2c79c05 FOREIGN KEY (login) REFERENCES my_user(login);


--
-- Name: fkc7fd802598677c78; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY preprocessing_redirect
    ADD CONSTRAINT fkc7fd802598677c78 FOREIGN KEY (owner_id) REFERENCES owner(id);


--
-- Name: fkce78835898677c78; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY language
    ADD CONSTRAINT fkce78835898677c78 FOREIGN KEY (owner_id) REFERENCES owner(id);


--
-- Name: fkdbc30f5b248802fd; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY search_index
    ADD CONSTRAINT fkdbc30f5b248802fd FOREIGN KEY (owner_id) REFERENCES owner(id);


--
-- PostgreSQL database dump complete
--

CREATE USER location_service_login WITH password 'location_service_login';
CREATE USER location_service_admin WITH password 'location_service_admin';
CREATE USER location_service WITH password 'location_service';

GRANT ALL PRIVILEGES ON DATABASE location_service to location_service_admin;

GRANT CONNECT ON DATABASE location_service to location_service;
GRANT USAGE ON SCHEMA public TO location_service;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO location_service;
GRANT INSERT ON search_event TO location_service;

GRANT CONNECT ON DATABASE location_service to location_service_login;
GRANT USAGE ON SCHEMA public TO location_service_login;
GRANT SELECT ON my_user, user_info TO location_service_login;
