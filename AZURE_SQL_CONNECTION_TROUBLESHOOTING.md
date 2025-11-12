# Azure SQL Database Connection Troubleshooting

## Error: Login failed for user 'sairajadmin@sairaj-sqlserver'

This error typically occurs due to one of the following reasons:

### 1. Azure SQL Database Firewall Rules (Most Common)

Azure SQL Database blocks all connections by default unless the client IP is whitelisted. Render's servers need to be allowed.

**Solution:**
1. Go to [Azure Portal](https://portal.azure.com)
2. Navigate to your SQL Server: `sairaj-sqlserver`
3. Go to **Security** → **Networking** (or **Firewalls and virtual networks**)
4. Add firewall rules:
   - **Option A**: Enable "Allow Azure services and resources to access this server" (checkbox)
   - **Option B**: Add specific IP ranges for Render (contact Render support for their IP ranges)
   - **Option C**: Temporarily add "0.0.0.0 - 255.255.255.255" for testing (NOT RECOMMENDED for production)

### 2. Incorrect Database Credentials

Verify the credentials in Azure Portal:
1. Go to Azure Portal → SQL Server → **Active Directory admin** or **SQL authentication**
2. Verify username: `sairajadmin`
3. Reset password if needed
4. Update `SPRING_DATASOURCE_PASSWORD` in `render.yaml` if changed

### 3. Database Name Verification

Ensure the database name matches:
- Expected: `SairajTravelsDB`
- Check in Azure Portal → SQL databases

### 4. Connection String Format

The username format for Azure SQL Database should be:
- Format: `username@servername` (e.g., `sairajadmin@sairaj-sqlserver`)
- This is already correctly configured

### 5. SSL/TLS Configuration

The connection string includes SSL settings:
- `encrypt=true`
- `trustServerCertificate=true`
- `sslProtocol=TLSv1.2`

If issues persist, try temporarily setting `encrypt=false` for testing (NOT recommended for production).

## Quick Fix Steps

1. **Enable Azure Services Access** (Easiest):
   - Azure Portal → SQL Server → Networking
   - Check "Allow Azure services and resources to access this server"
   - Save

2. **Verify Credentials**:
   - Test connection using Azure Data Studio or SQL Server Management Studio
   - Use the same credentials as in `render.yaml`

3. **Check Database Status**:
   - Ensure database is online and not paused
   - Verify service tier supports connections

4. **Review Logs**:
   - Check Azure SQL Server audit logs for connection attempts
   - Review Render deployment logs for detailed error messages

## Environment Variables

The application uses these environment variables (set in `render.yaml`):
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

These override the values in `application-prod.properties`.

## Testing Connection Locally

To test the connection string locally:
```bash
# Using sqlcmd (if installed)
sqlcmd -S sairaj-sqlserver.database.windows.net -U sairajadmin@sairaj-sqlserver -P "Deep@6044" -d SairajTravelsDB
```

## Additional Resources

- [Azure SQL Database Firewall Rules](https://docs.microsoft.com/en-us/azure/azure-sql/database/firewall-configure)
- [Render Documentation](https://render.com/docs)
- [Spring Boot Database Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/data.html#data.sql.datasource)

