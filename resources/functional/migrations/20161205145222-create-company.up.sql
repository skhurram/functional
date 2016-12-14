CREATE TABLE company_tbl(
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  company VARCHAR(100) NOT NULL,
  description VARCHAR(200) NOT NULL,
  email VARCHAR(100) NOT NULL,
  city VARCHAR(40) NOT NULL,
  country VARCHAR(40) NOT NULL,
  website VARCHAR(40) NOT NULL,
  registration_date DATE,
  created_at DATE,
  updated_at DATE,
  active BIT(1),
  PRIMARY KEY ( id ));