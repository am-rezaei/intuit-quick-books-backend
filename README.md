
# Quickbooks
Sample application showing how to Authenticate and fetch list of added/modified bills in the system

- Query used to fetch the new bills:
  `select * from bill where metadata.createtime > '%s'`

- Query used to fetch the modified bills:
  `select * from bill where metadata.lastupdatedtime > '%s' and metadata.createtime < '%s'`

- Query used to fetch all existing bills:
  `select * from bill `

**For the sake of simplicity the logic to handle refresh token and getting new access tokens is not implemented**