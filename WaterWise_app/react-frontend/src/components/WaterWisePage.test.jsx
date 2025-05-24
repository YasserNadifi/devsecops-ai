import React from "react";
import { render, screen } from "@testing-library/react";
import { describe,it,expect } from "vitest";
import "@testing-library/jest-dom/vitest"
import { MemoryRouter } from "react-router-dom";

import WaterWisePage from "./WaterWisePage";

describe("WaterWisaPage",()=>{
    it("testing title", ()=>{
        render(
            <MemoryRouter>
                <WaterWisePage />
            </MemoryRouter>
        );
        expect(screen.getByText("WaterWise")).toBeInTheDocument();
    });
});