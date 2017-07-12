

-- name: foo
select *  from Job where id = :id;

-- Some comment

-- name: create-animal<!
-- Adds animals to our menagerie.
-- (Not all that useful)
insert into animal_logs (name) values (:name);


-- This bears no meaning.
