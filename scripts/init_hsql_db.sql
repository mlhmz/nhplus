-- Caregiver Table Creation
CREATE TABLE IF NOT EXISTS caregiver (
      cid INT IDENTITY PRIMARY KEY,
      firstname VARCHAR(60),
      surname VARCHAR(60),
      telephone VARCHAR(60)
);
