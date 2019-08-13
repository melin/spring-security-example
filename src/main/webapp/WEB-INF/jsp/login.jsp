<body>
<div class="container">
    <h1>This is secured!</h1>
    <p>
        Hello Login
    </p>

    <form action="/login" method="post">
        <div><label> User Name : <input type="text" name="username"/> </label></div>
        <div><label> Password: <input type="password" name="password"/> </label></div>
        <div><input type="submit" value="Sign In"/></div>
    </form>
</div>
</body>
