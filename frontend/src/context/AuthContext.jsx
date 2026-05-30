import { createContext, useEffect, useMemo, useState } from 'react';
import { authService } from '../services/authService';
import { clearAuthStorage, getStoredToken, getStoredUser, setAuthStorage } from '../utils/auth';

export const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(getStoredToken());
  const [user, setUser] = useState(getStoredUser());
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!token) {
      setUser(null);
    }
  }, [token]);

  const login = async (payload) => {
    setLoading(true);
    try {
      const response = await authService.login(payload);
      setAuthStorage(response.token, response.user);
      setToken(response.token);
      setUser(response.user);
      return response.user;
    } finally {
      setLoading(false);
    }
  };

  const register = async (payload) => {
    setLoading(true);
    try {
      const response = await authService.register(payload);
      setAuthStorage(response.token, response.user);
      setToken(response.token);
      setUser(response.user);
      return response.user;
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    clearAuthStorage();
    setToken(null);
    setUser(null);
  };

  const value = useMemo(
    () => ({
      token,
      user,
      loading,
      isAuthenticated: Boolean(token),
      isAdmin: user?.roles?.includes('ROLE_ADMIN'),
      login,
      register,
      logout,
    }),
    [loading, token, user],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
