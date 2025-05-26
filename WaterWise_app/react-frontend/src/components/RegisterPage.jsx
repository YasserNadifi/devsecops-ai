import React, { useState } from "react";
import axios from "axios";
import {
  AtSymbolIcon,
  KeyIcon,
  ExclamationCircleIcon,
} from "@heroicons/react/24/outline";
import { ArrowRightIcon } from "@heroicons/react/20/solid";
import { Link } from "react-router-dom";

export const RegisterPage = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [isPending, setIsPending] = useState(false);
  const API_URL = import.meta.env.VITE_API_URL;

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

  async function handleSubmit(e) {
    e.preventDefault();
    if (password !== confirm) {
      setErrorMessage("Passwords do not match");
      return;
    }
    setErrorMessage("");
    setIsPending(true);

    try {
      const response = await axios.post(`${API_URL}/api/users/register`, { username : username,password : password });
      console.log(response.data)
      if (response.status === 200) {
        localStorage.setItem('current_user',response.data.id)
        window.location.href = "/dashboard";
      } else {
        setErrorMessage("Registration failed. Please try again.");
      }
    } catch (error) {
      // add exception handler for different errors later
      setErrorMessage(
        error.response?.data?.message || "Something went wrong"
      );
    } finally {
      setIsPending(false);
    }
  }

  return (
    <div className="w-full min-h-screen bg-green-50 flex items-center justify-center p-6 md:p-12">
      <section className="w-full max-w-xl flex justify-center">
        <form
          onSubmit={handleSubmit}
          className="bg-white rounded-2xl shadow-md p-6 md:p-10 w-full"
        >
          <h1 className="text-2xl md:text-4xl font-bold text-green-800 font-serif text-center mb-4">
            Sign up
          </h1>

          
          <label
            htmlFor="username"
            className="block text-gray-900 text-xs font-medium mb-2"
          >
            Username
          </label>
          <div className="relative mb-6">
            <input
              id="username"
              name="username"
              placeholder="Enter your username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              className="peer block w-full rounded-md border border-gray-200 py-[9px] pl-10 text-sm outline-2 placeholder:text-gray-500"
            />
            <AtSymbolIcon className="pointer-events-none absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
          </div>

          {/* Password */}
          <label
            htmlFor="password"
            className="block text-gray-900 text-xs font-medium mb-2"
          >
            Password
          </label>
          <div className="relative mb-6">
            <input
              id="password"
              type="password"
              name="password"
              placeholder="Enter password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              minLength={6}
              className="peer block w-full rounded-md border border-gray-200 py-[9px] pl-10 text-sm outline-2 placeholder:text-gray-500"
            />
            <KeyIcon className="pointer-events-none absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
          </div>

          {/* Confirm Password */}
          <label
            htmlFor="confirm"
            className="block text-gray-900 text-xs font-medium mb-2"
          >
            Confirm Password
          </label>
          <div className="relative mb-6">
            <input
              id="confirm"
              type="password"
              name="confirm"
              placeholder="Re-enter password"
              value={confirm}
              onChange={(e) => setConfirm(e.target.value)}
              required
              minLength={6}
              className="peer block w-full rounded-md border border-gray-200 py-[9px] pl-10 text-sm outline-2 placeholder:text-gray-500"
            />
            <KeyIcon className="pointer-events-none absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
          </div>

          {/* Submit */}
          <button
            type="submit"
            disabled={isPending}
            aria-disabled={isPending}
            className="w-full bg-green-500 hover:bg-green-600 text-white font-medium py-3 rounded-lg disabled:opacity-50 flex items-center justify-center gap-2"
          >
            Register <ArrowRightIcon className="h-5 w-5" />
          </button>

          {/* Error message */}
          <div
            className="flex h-8 items-center mt-4 space-x-2"
            aria-live="polite"
            aria-atomic="true"
          >
            {errorMessage && (
              <>
                {/* <ExclamationCircleIcon className="h-5 w-5 text-red-500" /> */}
                <h5 className="text-red-500 text-center">{errorMessage}</h5>
              </>
            )}
          </div>

          {/* Link to login */}
          <p className="mt-6 text-center text-sm text-gray-600">
            Already have an account?{" "}
            <Link
              to="/login"
              className="text-green-700 hover:text-green-900 font-medium"
            >
              Log in
            </Link>
          </p>
        </form>
      </section>
    </div>
  );
};
