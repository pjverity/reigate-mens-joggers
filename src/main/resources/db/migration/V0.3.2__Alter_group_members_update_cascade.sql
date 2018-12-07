
-- To allow user names (aka email addresses) to be modified in the 'users' table without
-- breaking the foreign key constraint in the group_members table. This is so the new
-- admin@reigatemensjoggers.co.uk username can be used without the need to delete
-- existing admin accounts and create a new admin account.
ALTER TABLE public.group_members DROP CONSTRAINT group_members_users_username_fkey;
ALTER TABLE public.group_members
  ADD CONSTRAINT group_members_users_username_fkey
FOREIGN KEY (username) REFERENCES users (username) ON DELETE CASCADE ON UPDATE CASCADE;