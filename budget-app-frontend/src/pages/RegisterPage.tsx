import React, { useState } from "react"
import { useNavigate, Link } from "react-router-dom"
import { apiFetch } from "../services/api"

const RegisterPage: React.FC = () => {
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [errorMessage, setErrorMessage] = useState("")
  const navigate = useNavigate()

  const handleRegister = async (e: React.SubmitEvent) => {
    e.preventDefault()
    setErrorMessage("")

    try {
      await apiFetch("/auth/register", {
        method: "POST",
        body: JSON.stringify({ email, password }),
      })

      alert("Konto skapat! Nu kan du logga in.")
      navigate("/login")
    } catch (err: unknown) {
      if (err instanceof Error) {
        setErrorMessage(err.message)
      } else {
        setErrorMessage("Registreringen misslyckades.")
      }
    }
  }

  return (
    <div className="card">
      <h2>Registrera konto</h2>
      {errorMessage && <p className="error-message">{errorMessage}</p>}

      <form onSubmit={handleRegister}>
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

        <button type="submit">Registrera</button>
      </form>

      <p className="auth-footer">
        Har du redan ett konto? <Link to="/login">Logga in här</Link>
      </p>
    </div>
  )
}

export default RegisterPage
