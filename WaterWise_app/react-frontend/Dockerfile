# Stage 1: Build Vite app
FROM node:20 AS build

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm install

COPY . .

# Accept the build argument
ARG VITE_API_URL

# Pass the argument inline when building the Vite app
RUN VITE_API_URL=$VITE_API_URL npm run build

# Stage 2: Serve with Nginx
FROM nginx:alpine

# Clean default html directory
RUN rm -rf /usr/share/nginx/html/*

# Copy build files
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
