INSERT INTO perfil (id_perfil, nombre_usuario, apellidos, email, login, contrasenia) VALUES 
(1, 'Juan', 'Cabello Rodríguez', 'juan@gmail.com', 'juan_cabello', 'TwZ1FoNvAvNyzsoFkX0Rucfk92XHC+JvPQGkgYGD8U8='),
(2, 'Miguel Ángel', 'Díaz Díaz', 'migue@gmail.com', 'migue_diaz', 'qJhdWk7v9uFTgc4LDrmstifPtnMyHmALLW3B+HcrVc8='),
(3, 'Jesús Francisco', 'Dugarte Vargas', 'fran@gmail.com', 'fran_dugarte', 'KH4Bc9xXFStCkxijTRKmerqoxBRENtb9ljsZW8P5nCg='),
(4, 'Pablo', 'Espigares Fernández', 'pablo@gmail.com', 'pablo_espigares', 'OG8uUCi6/ICsf3ZvRQ3Jc/Mk6MljN+IW20zDxa1QDLM='),
(5, 'José Antonio', 'González Román', 'jose@gmail.com', 'jose_gonzalez', 'DK+INVVLLay2brT1ou5tLKYEphLOOAyn2Dpge6si/F8='),
(6, 'Álvaro', 'López Ruedas', 'alvaro@gmail.com', 'alvaro_lopez', '62qNrXqvHVhqKmgQM6NUD81PMj4UlBvrt3h6oVrByE4='),
(7, 'Raúl', 'Martín Ruiz', 'raul@gmail.com', 'raul_martin', '0GTVb4TN7wl4Uh94SO352jFmCOlf+rJLAXXQPtoGiRY='),
(8, 'Adrián', 'Martín Seco', 'peligroso@gmail.com', 'adrian_martin', '9VxjNoPrQxz/ZDAqlf2STJyy6/Z6qj8BPjbRYri8bNY='),
(9, 'Fernando', 'Martínez Vallecillos', 'fernando@gmail.com', 'fernando_martinez', 'YgzTeBnKhlnTla5+Mha1uGD0I+WAKnO4Sy6bgxs0u60='),
(10, 'Álvaro', 'Moreno Manzano', 'alvarito@gmail.com', 'alvarito_moreno', 'l4dNKi6k9ATu6k1ZjDxxtv//IzXd8IqUKNyiUKEat1o='),
(11, 'Adrián', 'Navarro Salmerón', 'adrian@gmail.com', 'adrian_navarro', 'h9sjhDPfLlaAPx9cwQg4Q1Mly1VqYEFiNCZUJOIMK7c='),
(12, 'Fernando', 'Ortega Castro', 'ferni@gmail.com', 'ferni_ortega', 'tr8KN5mL8DUkMCtwWfbbtW+dVlNykFMBujFmR5Z4zzo='),
(13, 'Belén', 'Robustillo Carmona', 'belen@gmail.com', 'belen_robustillo', 'rtQmwTfoAL6LRay23Mhq+yZev2e1JD0DNicmNtdXNt8=');

INSERT INTO artista (id_artista, nombre_artista) VALUES
(1, 'Desconocido'),
(2, 'Creepy Nuts'),
(3, 'Sia'),
(4, 'Alejandro Sanz'),
(5, 'Camela'),
(6, 'Estopa'),
(7, 'David Guetta'),
(8, 'Mago de Oz'),
(9, 'Fondo Flamenco'),
(10, 'Camarón'),
(11, 'Don Omar');

INSERT INTO estadistica (id_estadistica, num_reproducciones, veces_incluida_en_playlists) VALUES
(1, 0, 0),
(2, 0, 0),
(3, 0, 0),
(4, 0, 0),
(5, 0, 0),
(6, 0, 0),
(7, 0, 0),
(8, 0, 0),
(9, 0, 0),
(10, 0, 0),
(11, 0, 0),
(12, 0, 0),
(13, 0, 0),
(14, 0, 0),
(15, 0, 0),
(16, 0, 0),
(17, 0, 0),
(18, 0, 0),
(19, 0, 0),
(20, 0, 0);

INSERT INTO cancion (id_cancion, estadistica_id_estadistica, artista_id_artista, titulo, anio_publicacion, duracion_seg, file_size_bytes, ruta) VALUES
(1, 1, 2, 'Bling Bang Bang Born', 2024, 168, 4040757, 'canciones\\blingBangBangBorn.mp3'),
(2, 2, 3, 'Chandelier', 2014, 212, 5103981, 'canciones\\chandelier.mp3'),
(3, 3, 1, 'Coin', 1995, 2, 17109, 'canciones\\coin.mp3'),
(4, 4, 4, 'Corazón Partío', 1997, 300, 7203663, 'canciones\\corazonPartio.mp3'),
(5, 5, 5, 'Cuando zarpa el amor', 1994, 220, 3441403, 'canciones\\cuandoZarpaElAmor.mp3'),
(6, 6, 6, 'Demonios', 2001, 259, 6233517, 'canciones\\demonios.mp3'),
(7, 7, 1, 'Digimon', 1999, 96, 2324826, 'canciones\\digimon.mp3'),
(8, 8, 1, 'Dragon Ball', 1989, 110, 2651461, 'canciones\\dragonBall.mp3'),
(9, 9, 1, 'El hombre que rompió el corazón a un dragón', 2003, 17, 413289, 'canciones\\elHombreQueRompioElCorazonAUnDragon.mp3'),
(10, 10, 7, 'Im Good (Blue)', 2023, 174, 4197357, 'canciones\\imGood.mp3'),
(11, 11, 8, 'La costa del silencio', 2003, 280, 6742236, 'canciones\\laCostaDelSilencio.mp3'),
(12, 12, 8, 'La posada de los muertos', 2005, 287, 6903986, 'canciones\\laPosadaDeLosMuertos.mp3'),
(13, 13, 9, 'Mi estrella blanca', 2006, 234, 5620077, 'canciones\\miEstrellaBlanca.mp3'),
(14, 14, 1, 'Que me quedo sin comer', 1999, 35, 864058, 'canciones\\queMeQuedoSinComer.mp3'),
(15, 15, 10, 'Soy gitano', 1989, 254, 6139629, 'canciones\\soyGitano.mp3'),
(16, 16, 11, 'Taboo', 2006, 286, 6882044, 'canciones\\taboo.mp3'),
(17, 17, 1, 'Tengo una pistola', 2014, 13, 329279, 'canciones\\tengoUnaPistola.mp3'),
(18, 18, 6, 'Tu calorro', 1999, 190, 4576163, 'canciones\\tuCalorro.mp3'),
(19, 19, 3, 'Unstoppable', 2016, 220, 5282723, 'canciones\\unstoppable.mp3'),
(20, 20, 11, 'Virtual Diva', 2009, 242, 5830667, 'canciones\\virtualDiva.mp3');

-- IDs extra para canciones propias
-- Juan: 21, 22, 23
-- Miguel Ángel: 24, 25, 26
-- Jesús Francisco: 27, 28, 29
-- José Antonio: 33, 34, 35
-- Álvaro López: 36, 37, 38
-- Raúl: 39, 40, 41
-- Adrián Martín: 42, 43, 44
-- Fernando Martínez: 45, 46, 47
-- Álvaro Moreno: 48, 49, 50
-- Adrián Navarro: 51, 52, 53
-- Fernando Ortega: 54, 55, 56
-- Belén: 57, 58, 59