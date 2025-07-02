-- Users 테이블 (Java @Table("Users"))
CREATE TABLE IF NOT EXISTS Users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(50)     NOT NULL UNIQUE,
    password VARCHAR(255)   NOT NULL,
    nickname VARCHAR(100),
    email VARCHAR(255)      NOT NULL UNIQUE,
    phone VARCHAR(20),
    status INT              NOT NULL DEFAULT 1,
    password_changed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_date DATETIME   NOT NULL,
    modified_date DATETIME  NOT NULL
);

-- comment 테이블
CREATE TABLE IF NOT EXISTS comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    board_id INT            NOT NULL,
    nickname VARCHAR(100)   NOT NULL,
    content TEXT            NOT NULL,
    status INT              NOT NULL DEFAULT 1,
    created_date DATETIME   NOT NULL,
    modified_date DATETIME  NOT NULL
);

-- reply 테이블
CREATE TABLE IF NOT EXISTS reply (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    comment_id BIGINT       NOT NULL,
    nickname VARCHAR(100)   NOT NULL,
    content TEXT            NOT NULL,
    status INT              NOT NULL DEFAULT 1,
    created_date DATETIME   NOT NULL,
    modified_date DATETIME  NOT NULL,
    FOREIGN KEY (comment_id) REFERENCES comment(id)
);

-- 카테고리 테이블
CREATE TABLE IF NOT EXISTS category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    parent_id BIGINT NULL,
    UNIQUE KEY uk_category_parent(parent_id, name),
    CONSTRAINT fk_category_parent
    FOREIGN KEY (parent_id) REFERENCES category(id)
    ON DELETE CASCADE
    );

-- product 테이블
CREATE TABLE IF NOT EXISTS product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    productname VARCHAR(100) NOT NULL,
    productnumber BIGINT NOT NULL,
    brandname VARCHAR(100) NOT NULL,
    likecount INT DEFAULT 0,
    gender INT NOT NULL,
    stock_count INT NOT NULL,
    category_id BIGINT NOT NULL,
    created_date DATETIME   NOT NULL,
    modified_date DATETIME  NOT NULL,
    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id) REFERENCES category(id)
);

-- 사이즈 체계
CREATE TABLE IF NOT EXISTS size_system (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

-- 사이즈 값
CREATE TABLE IF NOT EXISTS size_value (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    system_id BIGINT NOT NULL,
    code VARCHAR(10) NOT NULL,
    numeric_order DECIMAL(5, 2) NOT NULL,
    UNIQUE KEY uk_size_value (system_id, code),
    CONSTRAINT fk_size_value_system
        FOREIGN KEY (system_id) REFERENCES size_system(id)
        ON DELETE CASCADE
);

-- 상품과 사이즈 매핑
CREATE TABLE IF NOT EXISTS product_size (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    size_value_id BIGINT NOT NULL,
    UNIQUE KEY uk_product_size (product_id, size_value_id),
    CONSTRAINT fk_product_size_product
        FOREIGN KEY (product_id) REFERENCES product(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_product_size_value
        FOREIGN KEY (size_value_id) REFERENCES size_value(id)
        ON DELETE CASCADE
);

-- 상품 이미지 테이블
CREATE TABLE IF NOT EXISTS product_image (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    url VARCHAR(255) NOT NULL,
    alt_text VARCHAR(255) NOT NULL,
    sort_order TINYINT NOT NULL DEFAULT 0,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE KEY uk_product_image_primary (product_id, is_primary),
    CONSTRAINT fk_product_image_product
        FOREIGN KEY (product_id) REFERENCES product(id)
);

-- 측정 항목 테이블
CREATE TABLE IF NOT EXISTS measurement_attribute (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    unit VARCHAR(10) NOT NULL,
    UNIQUE KEY uk_attr_name (name)
);

-- 카테고리 별 사용 항목 매핑
CREATE TABLE IF NOT EXISTS category_attribute (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    attribute_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    UNIQUE KEY uk_cat_attr (category_id, attribute_id),
    CONSTRAINT fk_cat_attr_attr
        FOREIGN KEY (attribute_id) REFERENCES measurement_attribute(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_cat_attr_category
        FOREIGN KEY (category_id) REFERENCES category(id)
);

-- 사이즈 별 길이값 저장
CREATE TABLE IF NOT EXISTS product_size_measurement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_size_id BIGINT NOT NULL,
    attribute_id BIGINT NOT NULL,
    value DECIMAL(7, 2) NOT NULL,
    UNIQUE KEY uk_psm (product_size_id, attribute_id),
    CONSTRAINT fk_psm_ps
        FOREIGN KEY (product_size_id) REFERENCES product_size(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_psm_attr
        FOREIGN KEY (attribute_id) REFERENCES measurement_attribute(id)
        ON DELETE CASCADE
);

    -- ──────────────────────────────
--  ‘신발’ 카테고리 계층 데이터 (INSERT IGNORE)
-- ──────────────────────────────

-- 1) 최상위 ‘신발’
    INSERT IGNORE INTO category (name, parent_id)
VALUES ('신발', NULL);

-- 2) 2차 카테고리 (‘신발’의 자식)
INSERT IGNORE INTO category (name, parent_id)
SELECT * FROM (
                  SELECT '스니커즈'      AS name, (SELECT id FROM category WHERE name='신발') AS parent_id UNION ALL
                  SELECT '패딩/퍼 신발', (SELECT id FROM category WHERE name='신발') UNION ALL
                  SELECT '부츠/워커',    (SELECT id FROM category WHERE name='신발') UNION ALL
                  SELECT '구두',         (SELECT id FROM category WHERE name='신발') UNION ALL
                  SELECT '샌들/슬리퍼',  (SELECT id FROM category WHERE name='신발') UNION ALL
                  SELECT '스포츠화',     (SELECT id FROM category WHERE name='신발') UNION ALL
                  SELECT '신발용품',     (SELECT id FROM category WHERE name='신발')
              ) AS tmp;

-- 3) 3차 카테고리: 스니커즈 하위
INSERT IGNORE INTO category (name, parent_id)
SELECT * FROM (
                  SELECT '캔버스/단화',    (SELECT id FROM category WHERE name='스니커즈') UNION ALL
                  SELECT '스니커즈 뮬',    (SELECT id FROM category WHERE name='스니커즈') UNION ALL
                  SELECT '슬립온',         (SELECT id FROM category WHERE name='스니커즈') UNION ALL
                  SELECT '패션스니커즈화', (SELECT id FROM category WHERE name='스니커즈') UNION ALL
                  SELECT '기타 스니커즈',  (SELECT id FROM category WHERE name='스니커즈') UNION ALL
                  SELECT '스케이트 보드화',(SELECT id FROM category WHERE name='스니커즈')
              ) AS tmp;

-- 4) 3차 카테고리: 패딩/퍼 신발 하위
INSERT IGNORE INTO category (name, parent_id)
SELECT * FROM (
                  SELECT '패딩/퍼 부츠',  (SELECT id FROM category WHERE name='패딩/퍼 신발') UNION ALL
                  SELECT '패딩/퍼 슬리퍼',(SELECT id FROM category WHERE name='패딩/퍼 신발')
              ) AS tmp;

-- 5) 3차 카테고리: 부츠/워커 하위
INSERT IGNORE INTO category (name, parent_id)
SELECT * FROM (
                  SELECT '앵클/숏 부츠',  (SELECT id FROM category WHERE name='부츠/워커') UNION ALL
                  SELECT '미들/하프 부츠',(SELECT id FROM category WHERE name='부츠/워커') UNION ALL
                  SELECT '롱 부츠',       (SELECT id FROM category WHERE name='부츠/워커') UNION ALL
                  SELECT '니하이 부츠',    (SELECT id FROM category WHERE name='부츠/워커') UNION ALL
                  SELECT '레인 부츠',      (SELECT id FROM category WHERE name='부츠/워커') UNION ALL
                  SELECT '워커',          (SELECT id FROM category WHERE name='부츠/워커') UNION ALL
                  SELECT '기타 부츠',      (SELECT id FROM category WHERE name='부츠/워커')
              ) AS tmp;

-- 6) 3차 카테고리: 구두 하위
INSERT IGNORE INTO category (name, parent_id)
SELECT * FROM (
                  SELECT '더비 슈즈',      (SELECT id FROM category WHERE name='구두') UNION ALL
                  SELECT '몽크 스트랩',    (SELECT id FROM category WHERE name='구두') UNION ALL
                  SELECT '스트레이트 팁',  (SELECT id FROM category WHERE name='구두') UNION ALL
                  SELECT '윙 팁',          (SELECT id FROM category WHERE name='구두') UNION ALL
                  SELECT '플레인 토',      (SELECT id FROM category WHERE name='구두') UNION ALL
                  SELECT '로퍼',           (SELECT id FROM category WHERE name='구두') UNION ALL
                  SELECT '보트 슈즈',      (SELECT id FROM category WHERE name='구두') UNION ALL
                  SELECT '모카신',         (SELECT id FROM category WHERE name='구두') UNION ALL
                  SELECT '블로퍼',         (SELECT id FROM category WHERE name='구두') UNION ALL
                  SELECT '힐/펌프스',       (SELECT id FROM category WHERE name='구두') UNION ALL
                  SELECT '메리제인 슈즈',   (SELECT id FROM category WHERE name='구두') UNION ALL
                  SELECT '슬링백',         (SELECT id FROM category WHERE name='구두') UNION ALL
                  SELECT '웨지힐',         (SELECT id FROM category WHERE name='구두') UNION ALL
                  SELECT '플랫폼',         (SELECT id FROM category WHERE name='구두') UNION ALL
                  SELECT '플랫 슈즈',       (SELECT id FROM category WHERE name='구두') UNION ALL
                  SELECT '기타 구두',       (SELECT id FROM category WHERE name='구두')
              ) AS tmp;

-- 7) 3차 카테고리: 샌들/슬리퍼 하위
INSERT IGNORE INTO category (name, parent_id)
SELECT * FROM (
                  SELECT '클로그',         (SELECT id FROM category WHERE name='샌들/슬리퍼') UNION ALL
                  SELECT '쪼리/플립플랍',   (SELECT id FROM category WHERE name='샌들/슬리퍼') UNION ALL
                  SELECT '스포츠/캐주얼 샌들',(SELECT id FROM category WHERE name='샌들/슬리퍼') UNION ALL
                  SELECT '슬링백 샌들',     (SELECT id FROM category WHERE name='샌들/슬리퍼') UNION ALL
                  SELECT '스트랩 샌들',     (SELECT id FROM category WHERE name='샌들/슬리퍼') UNION ALL
                  SELECT '스트랩 슬리퍼',   (SELECT id FROM category WHERE name='샌들/슬리퍼') UNION ALL
                  SELECT '슬라이드',       (SELECT id FROM category WHERE name='샌들/슬리퍼') UNION ALL
                  SELECT '실내화',         (SELECT id FROM category WHERE name='샌들/슬리퍼') UNION ALL
                  SELECT '아웃도어 샌들',   (SELECT id FROM category WHERE name='샌들/슬리퍼') UNION ALL
                  SELECT '기타 슬리퍼',     (SELECT id FROM category WHERE name='샌들/슬리퍼') UNION ALL
                  SELECT '기타 샌들',       (SELECT id FROM category WHERE name='샌들/슬리퍼')
              ) AS tmp;

-- 8) 3차 카테고리: 스포츠화 하위
INSERT IGNORE INTO category (name, parent_id)
SELECT * FROM (
                  SELECT '테니스화',       (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '야구화',         (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '등산화',         (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '트레킹화',       (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '축구화',         (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '아쿠아 슈즈',     (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '농구화',         (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '런닝화',         (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '인도어화',       (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '자전거화',       (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '피트니스화',     (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '다이빙 부츠',    (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '골프화',         (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '바이크 부츠',    (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '부츠/전술화',     (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '워터스포츠화',   (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '복싱/격투화',     (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '탁구화',         (SELECT id FROM category WHERE name='스포츠화') UNION ALL
                  SELECT '기타 운동화',     (SELECT id FROM category WHERE name='스포츠화')
              ) AS tmp;

-- 9) 3차 카테고리: 신발용품 하위
INSERT IGNORE INTO category (name, parent_id)
SELECT * FROM (
                  SELECT '구두약/염색제',   (SELECT id FROM category WHERE name='신발용품') UNION ALL
                  SELECT '구두솔/브러시',   (SELECT id FROM category WHERE name='신발용품') UNION ALL
                  SELECT '스프레이/탈취제', (SELECT id FROM category WHERE name='신발용품') UNION ALL
                  SELECT '신발 커버',       (SELECT id FROM category WHERE name='신발용품') UNION ALL
                  SELECT '구둣 주걱',       (SELECT id FROM category WHERE name='신발용품') UNION ALL
                  SELECT '슈트리/부츠키퍼', (SELECT id FROM category WHERE name='신발용품') UNION ALL
                  SELECT '보호 쿠션/패드',   (SELECT id FROM category WHERE name='신발용품') UNION ALL
                  SELECT '깔창',           (SELECT id FROM category WHERE name='신발용품') UNION ALL
                  SELECT '신발끈',         (SELECT id FROM category WHERE name='신발용품') UNION ALL
                  SELECT '기타 신발용품',   (SELECT id FROM category WHERE name='신발용품')
              ) AS tmp;
