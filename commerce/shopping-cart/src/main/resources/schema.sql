CREATE TABLE IF NOT EXISTS cart (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_item (
    cart_id UUID DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (cart_id, product_id),
    CONSTRAINT fk_cart
    FOREIGN KEY (cart_id)
    REFERENCES cart (id)
    ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_cart_user_name ON cart (user_name);