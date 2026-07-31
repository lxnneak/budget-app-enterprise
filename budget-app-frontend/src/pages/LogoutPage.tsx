import React, { useEffect } from "react"
import { useNavigate, Link } from "react-router-dom"

const LogoutPage: React.FC = () => {
  const navigate = useNavigate()

  useEffect(() => {
    const timer = setTimeout(() => navigate("/login"), 2500)
    return () => clearTimeout(timer)
  }, [navigate])

  return (
    <div className="card" style={{ textAlign: "center" }}>
      <h2>Du har loggats ut</h2>
      <p>Du skickas vidare till inloggningen...</p>
      <Link to="/login">Klicka här om det tar för lång tid</Link>
    </div>
  )
}

export default LogoutPage
