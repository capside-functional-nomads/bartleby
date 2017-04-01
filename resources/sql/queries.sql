-- :name find-all-tasks :? :*
select id, description, done from todo

-- :name find-incomplete-tasks :? *
select id, description, done from todo where not done
