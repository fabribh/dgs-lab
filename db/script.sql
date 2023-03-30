CREATE TABLE IF NOT EXISTS public."user_tb"
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) COLLATE pg_catalog."default" NOT NULL,
    password VARCHAR(100) COLLATE pg_catalog."default" NOT NULL,
    email VARCHAR(50) COLLATE pg_catalog."default" NOT NULL
    )

ALTER TABLE IF EXISTS public."user_tb"
    OWNER to postgres;