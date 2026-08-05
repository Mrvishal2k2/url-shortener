CREATE TABLE urls
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    url         VARCHAR(2048) NOT NULL,
    short_id    VARCHAR(10)   NOT NULL,
    created_at  datetime(6)           NOT NULL,
    expires_at  datetime(6)           NULL,
    click_count BIGINT        NOT NULL,
    is_active   BIT(1)        NOT NULL,
    CONSTRAINT pk_urls PRIMARY KEY (id)
);

ALTER TABLE urls
    ADD CONSTRAINT uc_urls_shortid UNIQUE (short_id);

CREATE INDEX idx_short_id ON urls (short_id);