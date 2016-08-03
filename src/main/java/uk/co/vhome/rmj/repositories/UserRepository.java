package uk.co.vhome.rmj.repositories;

import uk.co.vhome.rmj.model.User;

import java.util.Collection;

public interface UserRepository
{
	Collection<User> getAll();
}