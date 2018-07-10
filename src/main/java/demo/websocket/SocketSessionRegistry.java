package demo.websocket;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.util.Assert;

public class SocketSessionRegistry {

	private final ConcurrentMap<String, Set<String>> userSessionIds = new ConcurrentHashMap<String, Set<String>>();
	private final Object lock = new Object();

	public SocketSessionRegistry() {
	}

	/**
	 *
	 * @param user
	 * @return
	 */
	public Set<String> getSessionIds(String user) {
		Set<String> set = (Set<String>) this.userSessionIds.get(user);
		return set != null ? set : Collections.emptySet();
	}

	public ConcurrentMap<String, Set<String>> getAllSessionIds() {
		return this.userSessionIds;
	}

	/**
	 * register session
	 * 
	 * @param user
	 * @param sessionId
	 */
	@SuppressWarnings("unchecked")
	public void registerSessionId(String user, String sessionId) {
		Assert.notNull(user, "User must not be null");
		Assert.notNull(sessionId, "Session ID must not be null");
		synchronized (this.lock) {
			Object set = this.userSessionIds.get(user);
			if (set == null) {
				set = new CopyOnWriteArraySet<Object>();
				this.userSessionIds.put(user, (Set<String>) set);
			}

			((Set<String>) set).add(sessionId);
		}
	}

	public void unregisterSessionId(String userName, String sessionId) {
		Assert.notNull(userName, "User Name must not be null");
		Assert.notNull(sessionId, "Session ID must not be null");
		synchronized (this.lock) {
			Set<String> set = (Set<String>) this.userSessionIds.get(userName);
			if (set != null && set.remove(sessionId) && set.isEmpty()) {
				this.userSessionIds.remove(userName);
			}

		}
	}
}
