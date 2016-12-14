-- name: create-company<!
-- creates a new company record
INSERT INTO company_tbl
(first_name, last_name, company, description, email,
 city, country, website, registration_date, created_at,
 updated_at, active)
VALUES (:first_name, :last_name, :company, :description, :email,
        :city, :country, :website, :registration_date, :created_at,
        :updated_at, :active)

-- name: update-company!
-- update a company record
update company_tbl
set
  first_name = :first_name,
  last_name = :last_name,
  company = :company,
  description = :description,
  email = :email,
  city = :city,
  country = :country,
  website = :website,
  registration_date = :registration_date,
  created_at = :created_at,
  updated_at = :updated_at,
  active = :active
where id = :id

-- name: get-company
-- retrieve a company given the id.
SELECT * FROM company_tbl
WHERE id = :id

-- name: get-companies
SELECT * FROM company_tbl

-- name: delete-company!
-- delete a company given the id
DELETE FROM company_tbl
WHERE id = :id
