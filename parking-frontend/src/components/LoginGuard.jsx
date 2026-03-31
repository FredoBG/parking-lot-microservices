import React, { useState, useEffect } from 'react';
import { checkSession } from '../services/api';

const LoginGuard = ({ children }) => {
  // 1. MAKE SURE THESE TWO ARE DEFINED HERE!
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const verifySession = async () => {
      try {
        const response = await checkSession();
        console.log("Session verified:", response.data);

        // 2. This is what was crashing:
        setIsAuthenticated(true);
      } catch (error) {
        console.error("Session check failed:", error);
        if (error.response?.status === 401) {
          window.location.href = '/oauth2/authorization/keycloak';
        } else {
          // 3. Make sure this matches your state name
          setErrorMessage("BFF Service is offline.");
        }
      } finally {
        setIsLoading(false);
      }
    };
    verifySession();
  }, []);

  if (isLoading) return <div>Verifying session...</div>;
  if (errorMessage) return <div>{errorMessage}</div>;

  return isAuthenticated ? children : null;
};

export default LoginGuard;