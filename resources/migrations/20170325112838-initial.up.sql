create table todo (
  id serial primary key,
  description text not null,
  done boolean default false
)
