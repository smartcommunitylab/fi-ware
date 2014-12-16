import urllib
import urllib2
import json
import pprint
import sys

#Global var
ufrom = ''
uto = ''
authcode = ''

#Retrieve organization id from new Opendata portal
def retrieveOrganizationId():
	#Dump a dictionary to a string for posting
	data_org = urllib.quote(json.dumps({'id':'trentino'}))
	
	#Request
	try:
		response = urllib2.urlopen(uto+'/api/3/action/organization_show',data_org)
	except urllib2.HTTPError as e:
		print "An error occurred while retrieving 'trentino' organization data from opendata"
		print e.code
		print e.read()
		sys.exit(1)

	assert response.code == 200

	#Load CKAN response into a dictionary
	response_dict = json.loads(response.read())

	#Check contents of response
	assert response_dict['success'] is True
	result = response_dict['result']

	#Retrieve id of organization
	idOrg = result['id']
	print "Organization Trentino id is retrieved.."
	return idOrg

#Retrieve list of dataset from Opendata Trentino
def listOfDataset_OldPortal():

	#Request list
	try:
		response = urllib2.urlopen(ufrom+'/api/3/action/package_list')
	except urllib2.HTTPError as e:
		print "An error occurred while listing package dataset from trentino opendata"
		print e.code
		print e.read()
		sys.exit(1)

	assert response.code == 200

	#Load CKAN response into a dictionary
	response_dict = json.loads(response.read())
	
	#Check contents of response
	assert response_dict['success'] is True
	#Insert result in a list
	listPackage = response_dict['result']

	print "Package list retrieved from "+ ufrom

	return listPackage

def retrieveDataset(string):
	print string
	#Dump a dictionary to a string for posting
	data_string = urllib.quote(json.dumps({'id':string}))
	
	#Request
	try:
		response = urllib2.urlopen(ufrom+'/api/3/action/package_show', data_string)
	except urllib2.HTTPError as e:
		print "An error occurred while retrieving dataset data"
		print e.code
		print e.read()
		sys.exit(1)
    
	assert response.code == 200
	
	#Use json module to load CKAN's response into a dictionary
	response_dict = json.loads(response.read())
	
	#Check contents of response
	assert response_dict['success'] is True
	dataset = response_dict['result']
	
	print "Dataset retrieved from "+ufrom
        
        return dataset

def listOfGroup_OldPortal():
    #Request list
	try:
		response = urllib2.urlopen(ufrom+'/api/3/action/group_list')
	except urllib2.HTTPError as e:
		print "An error occurred while listing group from trentino opendata"
		print e.code
		print e.read()
		sys.exit(1)
    
	assert response.code == 200
    
	#Load CKAN response into a dictionary
	response_dict = json.loads(response.read())
	
	#Check contents of response
	assert response_dict['success'] is True
	#Insert result in a list
	listGroup = response_dict['result']
	
	print "List of Group retrieved from "+ufrom
    
	return listGroup

def retrieveGroup(string):
	print string
	#Dump a dictionary to a string for posting
	data_string = urllib.quote(json.dumps({'id':string, 'include_datasets':'False'}))
        
	#Request
	try:
        	response = urllib2.urlopen(ufrom+'/api/3/action/group_show', data_string)
	except urllib2.HTTPError as e:
        	print "An error occurred while retrieving group data"
        	print e.code
        	print e.read()
        	sys.exit(1)
        
    	assert response.code == 200
        
    	#Use json module to load CKAN's response into a dictionary
    	response_dict = json.loads(response.read())
        
   	#Check contents of response
    	assert response_dict['success'] is True
    	group = response_dict['result']

	print "Group retrieved from "+ufrom
    
	return group

def isGroupExist(string):
	print string
	#Dump a dictionary to a string for posting
	data_string = urllib.quote(json.dumps({'id':string, 'include_datasets':'False'}))
    
	#Request
	try:
        	response = urllib2.urlopen(uto+'/api/3/action/group_show', data_string)
	except urllib2.HTTPError as e:
       		print "An error occurred while checking if a group already exists"
        	print e.code
        	print e.read()
        	if e.code == 403:
            		print "Group Found but you have not authorization.."
            		return True
        	if e.code == 404:
           		print "Group Not Found.."
            		return False
    
	if response.code == 200:
        	print "Group Found.."
        	return True


def isPackageExist(string):
	print string
	#Dump a dictionary to a string for posting
	data_string = urllib.quote(json.dumps({'id':string}))
    
    	#Request
   	try:
        	response = urllib2.urlopen(uto+'/api/3/action/package_show', data_string)
    	except urllib2.HTTPError as e:
       		print "An error occurred while checking if a package already exists"
        	print e.code
        	print e.read()
        	if e.code == 403:
            		print "Package Found but you have not authorization.."
            		return True
        	if e.code == 404:
            		print "Package Not Found.."
            		return False
    
	if response.code == 200:
        	print "Package Found.."
        	return True

def update_group(object):

	#Use json module to dump the dictionary to a string for posting
	data_string = urllib.quote(json.dumps(object))

	#Request
        request = urllib2.Request(uto+'/api/3/action/group_update')

	#Add authorization header
	request.add_header('Authorization', authcode)

	#HTTP request
	try:
		response = urllib2.urlopen(request,data_string)
	except urllib2.HTTPError as e:
        	print "An error occurred while updating a new group"
        	print e.code
        	print e.read()
        	sys.exit(1)

	assert response.code == 200

	#Load CKAN's response into a dictionary (json module)
	response_dict = json.loads(response.read())
	assert response_dict['success'] is True

	#returns created group as result
	created_group = response_dict['result']
    
	print "Group updated in "+uto



def update_package(string,object):
    
	#Remove id element from object
	del object['id']
    
	#Change title
	title = object['title']
	object['title'] = 'Trentino: '+title
	#Change owner_org
	object['owner_org'] = string
	#Make it private
	#object['private'] = 'True'
    
	#Change tag if a vocabulary tag is found then delete vocabulary id (for now)
	tags = object['tags']
	for t in tags:
       		t['vocabulary_id'] = None
	object['tags'] = tags
        
	#Use json module to dump the dictionary to a string for posting
	data_string = urllib.quote(json.dumps(object))
    
	#Request
	request = urllib2.Request(uto+'/api/3/action/package_update')

	#Add authorization header
	request.add_header('Authorization', authcode)

	#HTTP request
	try:
		response = urllib2.urlopen(request,data_string)
	except urllib2.HTTPError as e:
		print "An error occurred while updating a new package (dataset)"
        	print e.code
        	print e.read()
        	sys.exit(1)

	assert response.code == 200

	#Load CKAN's response into a dictionary (json module)
	response_dict = json.loads(response.read())
	assert response_dict['success'] is True

	#returns created group as result
	created_group = response_dict['result']

	print "Package updated in "+uto

def setGlobalVariable(f,t,auth):
	global ufrom
	ufrom = f
	global uto 
	uto = t
	global authcode
	authcode = auth


def main():

	#setGlobalVariable('http://dati.trentino.it','https://data.lab.fi-ware.org','a349c0d5-6c98-4cd4-a5e0-283036621630')
	#print ufrom
	#print uto
	#print authcode

	print "Starting..."
    
	print "Retrieve Trentino id"
        orgId = retrieveOrganizationId()
        
        print "List group from trentino opendata"
        grouplist = listOfGroup_OldPortal()
        
        #Add all group
        print "Retrieve and update a group"
        for g in grouplist:
		print "-----------------------------------------------------"
		print "Group: "+g
		found = isGroupExist(g)
		if not found:
			print "Group not found, then cannot update..."
            	else:
                	print "Group already exists in "+uto
			print "Updating..."
			groupdata = retrieveGroup(g)
        		update_group(groupdata)
		print "-----------------------------------------------------"
            
        print "List package dataset from trentino opendata"
        datalist = listOfDataset_OldPortal()

        #Add dataset
        print "Retrieve and update a dataset"
        for p in datalist:
		print "-----------------------------------------------------"
		print "Package: "+p
		found = isPackageExist(p)
		if not found:
			print "Package not found, then cannot update..."
            	else:
                	print "Pacakage already exists in "+uto
			print "Updating..."
			datasetdata = retrieveDataset(p)
                	update_package(orgId,datasetdata)
		print "-----------------------------------------------------"

	print "Done!"


if __name__ == "__main__":
	main()
