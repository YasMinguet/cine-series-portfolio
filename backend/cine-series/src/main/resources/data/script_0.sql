-- ------------------------------
-- TABLAS
-- ------------------------------

CREATE TABLE IF NOT EXISTS Genre (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Series (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    release_year INT,
    poster_url VARCHAR(255),
    genre_id BIGINT,
    average_rating DECIMAL(3,1) DEFAULT 0.0,
    FOREIGN KEY (genre_id) REFERENCES Genre(id)
);

CREATE TABLE IF NOT EXISTS User (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER'
);

CREATE TABLE IF NOT EXISTS Rating (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    series_id BIGINT,
    score DECIMAL(3,1) CHECK (score >= 0 AND score <= 10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (series_id) REFERENCES Series(id),
    UNIQUE(user_id, series_id)
);

-- ------------------------------
-- DATOS DE EJEMPLO
-- ------------------------------

-- Géneros
INSERT INTO Genre (name) VALUES 
('Drama'), ('Comedia'), ('Thriller'), ('Ciencia Ficción'), ('Fantasía'), ('Documental');

-- Series
INSERT INTO Series (title, description, release_year, poster_url, genre_id) VALUES
('Stranger Things', 'Un grupo de niños descubre secretos sobrenaturales en su ciudad.', 2016, 'https://image.tmdb.org/t/p/w500/x2LSRK2Cm7MZhjluni1msVJ3wDF.jpg', 4),
('The Crown', 'Drama histórico sobre la vida de la reina Isabel II.', 2016, 'https://image.tmdb.org/t/p/w500/6HRZ8q9pAc2v8AxVf0R9NNH0lU.jpg', 1),
('Narcos', 'La historia de los carteles de drogas en Colombia.', 2015, 'https://image.tmdb.org/t/p/w500/oPqvMq8lhA0p06E0Z2h8Oj8xweP.jpg', 3),
('Black Mirror', 'Cada episodio explora la sociedad moderna y la tecnología.', 2011, 'https://image.tmdb.org/t/p/w500/fQvCSvSTd1u1mM0y38KHm4XZIk.jpg', 4),
('The Witcher', 'Geralt de Rivia lucha contra monstruos y se enfrenta a su destino.', 2019, 'https://image.tmdb.org/t/p/w500/3v9PbqYs0SlYgXffC2hXdfCw8s2.jpg', 5),
('BoJack Horseman', 'Comedia animada sobre un actor caído en desgracia en Hollywood.', 2014, 'https://image.tmdb.org/t/p/w500/6u1fYtxG5eqjhtCPDx04pJphQRW.jpg', 2),
('Our Planet', 'Documental sobre la naturaleza y la biodiversidad del planeta.', 2019, 'https://image.tmdb.org/t/p/w500/bMtZXjRW3F2zD4hA4vMZr2iXr1U.jpg', 6),
('Mindhunter', 'Agentes del FBI investigan la psicología de asesinos en serie.', 2017, 'https://image.tmdb.org/t/p/w500/kjlCczYLO7RlY3bfq8tt9kYpP0c.jpg', 3),
('Dark', 'Una pequeña ciudad descubre secretos y paradojas temporales.', 2017, 'https://image.tmdb.org/t/p/w500/y4mYwH5eVrXx4rBCVjvWqMwXQ0g.jpg', 4),
('The Queen’s Gambit', 'Una joven prodigio del ajedrez enfrenta sus demonios personales.', 2020, 'https://image.tmdb.org/t/p/w500/yeNw7e2Xc3Y2G0vwqGPOWzXK6Q.jpg', 1);

-- Usuarios (contraseñas hasheadas con BCrypt, password real: '123456')
INSERT INTO User (username, password, role) VALUES
('Antonia', '$2a$10$Dow1MfE4KfR/dX1C/0B1Eez8TO/ePdpWwLwYkPO9/pOiwE5DcF4Cy', 'USER'),
('admin', '$2a$10$Dow1MfE4KfR/dX1C/0B1Eez8TO/ePdpWwLwYkPO9/pOiwE5DcF4Cy', 'ADMIN'),
('Paquita', '$2a$10$Dow1MfE4KfR/dX1C/0B1Eez8TO/ePdpWwLwYkPO9/pOiwE5DcF4Cy', 'USER');

-- Ratings de ejemplo
INSERT INTO Rating (user_id, series_id, score) VALUES
(1, 1, 9.5),
(2, 1, 9.0),
(3, 2, 8.5),
(1, 3, 8.0),
(2, 4, 8.8),
(3, 5, 8.2),
(1, 6, 8.7),
(2, 7, 9.3),
(3, 8, 8.6),
(1, 9, 8.8),
(2, 10, 8.6);
