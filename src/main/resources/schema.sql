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
    likecount INT DEFAULT 0,
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
CREATE TABLE product_size_measurement (
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
)