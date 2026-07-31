import React, { useState } from "react"
import { useNavigate, Link } from "react-router-dom"
import { apiFetch } from "../services/api"

const LoginPage: React.FC = () => {
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [errorMessage, setErrorMessage] = useState("")
  const navigate = useNavigate()

  const handleLogin = async (e: React.SubmitEvent) => {
    e.preventDefault()
    setErrorMessage("")

    try {
      await apiFetch("/auth/login", {
        method: "POST",
        body: JSON.stringify({ email, password }),
      })

      navigate("/dashboard")
    } catch (err: unknown) {
      if (err instanceof Error) {
        setErrorMessage(err.message)
      } else {
        setErrorMessage("Inloggningen misslyckades.")
      }
    }
  }

  return (
    <div className="card">
      <h2>Logga in</h2>
      {errorMessage && <p className="error-message">{errorMessage}</p>}

      <form onSubmit={handleLogin}>
        <div>
          <label>E-post:</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>

        <div>
          <label>Lösenord:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <button type="submit">Logga in</button>
      </form>

      <p className="auth-footer">
        Har du inget konto? <Link to="/register">Registrera dig här</Link>
      </p>
    </div>
  )
}

export default LoginPage
