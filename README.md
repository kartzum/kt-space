# kt-space

## kt-k

* https://play.kotlinlang.org/koans/overview
* https://kotlinlang.org/docs/home.html

## sd
Spring Boot Demo.

* https://start.spring.io/

## sf

Spring Test Demo.

## cm
Compose Demo.

* https://www.jetbrains.com/lp/compose-mpp/

## kr1
ktor Demo.

* https://ktor.io/
* https://github.com/ktorio/ktor-documentation/tree/2.2.4/codeSnippets/snippets/tutorial-http-api

## dbqp
dbqp application.

* Based on: https://git.yoomoney.ru/projects/BACKEND-LIBRARIES/repos/db-queue/browse
* https://habr.com/ru/articles/481556/
* https://postgrespro.ru/docs/postgrespro/12/sql-createtable

```sql
CREATE TABLE dev.queue_tasks_o (
	id bigserial NOT NULL,
	queue_name text NOT NULL,
	payload text NULL,
	created_at timestamptz NULL DEFAULT now(),
	next_process_at timestamptz NULL DEFAULT now(),
	attempt int4 NULL DEFAULT 0,
	reenqueue_attempt int4 NULL DEFAULT 0,
	total_attempt int4 NULL DEFAULT 0,
	request_id text NULL,
	CONSTRAINT queue_tasks_o_pkey PRIMARY KEY (id)
)
WITH (
	fillfactor=30,
	autovacuum_vacuum_cost_delay=5,
	autovacuum_vacuum_cost_limit=500,
	autovacuum_vacuum_scale_factor=0.0001
);
```
```sql
CREATE UNIQUE INDEX queue_tasks_o_name_time_desc_idx ON dev.queue_tasks_o USING btree (queue_name, next_process_at, id DESC) WITH (fillfactor='30');
```

```sql
CREATE TABLE dev.queue_tasks_h_8 (
	id bigserial PRIMARY KEY,
	queue_name text NOT NULL,
	payload text NULL,
	created_at timestamptz NULL DEFAULT now(),
	next_process_at timestamptz NULL DEFAULT now(),
	attempt int4 NULL DEFAULT 0,
	reenqueue_attempt int4 NULL DEFAULT 0,
	total_attempt int4 NULL DEFAULT 0,
	request_id text NULL
)
PARTITION BY hash (id);
```
```sql
CREATE UNIQUE INDEX queue_tasks_h_8_name_time_desc_idx ON dev.queue_tasks_h_8 USING btree (queue_name, next_process_at, id DESC) WITH (fillfactor='30');

CREATE TABLE dev.queue_tasks_h_8_0 PARTITION OF dev.queue_tasks_h_8 FOR VALUES WITH (modulus 8, remainder 0);
CREATE TABLE dev.queue_tasks_h_8_1 PARTITION OF dev.queue_tasks_h_8 FOR VALUES WITH (modulus 8, remainder 1);
CREATE TABLE dev.queue_tasks_h_8_2 PARTITION OF dev.queue_tasks_h_8 FOR VALUES WITH (modulus 8, remainder 2);
CREATE TABLE dev.queue_tasks_h_8_3 PARTITION OF dev.queue_tasks_h_8 FOR VALUES WITH (modulus 8, remainder 3);
CREATE TABLE dev.queue_tasks_h_8_4 PARTITION OF dev.queue_tasks_h_8 FOR VALUES WITH (modulus 8, remainder 4);
CREATE TABLE dev.queue_tasks_h_8_5 PARTITION OF dev.queue_tasks_h_8 FOR VALUES WITH (modulus 8, remainder 5);
CREATE TABLE dev.queue_tasks_h_8_6 PARTITION OF dev.queue_tasks_h_8 FOR VALUES WITH (modulus 8, remainder 6);
CREATE TABLE dev.queue_tasks_h_8_7 PARTITION OF dev.queue_tasks_h_8 FOR VALUES WITH (modulus 8, remainder 7);


ALTER TABLE dev.queue_tasks_h_8_0 SET (fillfactor = 30, autovacuum_vacuum_cost_delay = 5, autovacuum_vacuum_cost_limit = 500, autovacuum_vacuum_scale_factor = 0.0001);
ALTER TABLE dev.queue_tasks_h_8_1 SET (fillfactor = 30, autovacuum_vacuum_cost_delay = 5, autovacuum_vacuum_cost_limit = 500, autovacuum_vacuum_scale_factor = 0.0001);
ALTER TABLE dev.queue_tasks_h_8_2 SET (fillfactor = 30, autovacuum_vacuum_cost_delay = 5, autovacuum_vacuum_cost_limit = 500, autovacuum_vacuum_scale_factor = 0.0001);
ALTER TABLE dev.queue_tasks_h_8_3 SET (fillfactor = 30, autovacuum_vacuum_cost_delay = 5, autovacuum_vacuum_cost_limit = 500, autovacuum_vacuum_scale_factor = 0.0001);
ALTER TABLE dev.queue_tasks_h_8_4 SET (fillfactor = 30, autovacuum_vacuum_cost_delay = 5, autovacuum_vacuum_cost_limit = 500, autovacuum_vacuum_scale_factor = 0.0001);
ALTER TABLE dev.queue_tasks_h_8_5 SET (fillfactor = 30, autovacuum_vacuum_cost_delay = 5, autovacuum_vacuum_cost_limit = 500, autovacuum_vacuum_scale_factor = 0.0001);
ALTER TABLE dev.queue_tasks_h_8_6 SET (fillfactor = 30, autovacuum_vacuum_cost_delay = 5, autovacuum_vacuum_cost_limit = 500, autovacuum_vacuum_scale_factor = 0.0001);
ALTER TABLE dev.queue_tasks_h_8_7 SET (fillfactor = 30, autovacuum_vacuum_cost_delay = 5, autovacuum_vacuum_cost_limit = 500, autovacuum_vacuum_scale_factor = 0.0001);
```
