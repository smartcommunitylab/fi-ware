from Tkinter import *
import tkFont
import createPackageGroup
import updatePackageGroup

class App(Frame):

	def __init__(self, master):
		Frame.__init__(self,master)
		self.place()

		frame = Frame(height=470,width=450)
		frame.place(relx=0.05,rely=0.0)

		#Label
		self.lframe = Label(frame,text="CKAN/CKAN Harvester",font=("Helvetica", 16))
		self.lframe.place(relx=0.4,rely=0.1,anchor=CENTER)
		
		f = tkFont.Font(self.lframe, self.lframe.cget("font"))
		f.configure(underline = True)
		self.lframe.configure(font=f)
		
		#Text - msg
		longtext = "Takes in input the URLs of two CKAN instances and\n copies the datasets of the first one into the second one.\n Can act as a create procedure or as an update procedure."
		self.subtitle = Label(frame, text=longtext, anchor=W, justify=CENTER, relief=RAISED, borderwidth=2)
		self.subtitle.place(relx=0.45,rely=0.2,anchor=CENTER)

		#Radiobutton
		self.v = StringVar()
		self.R1 = Radiobutton(frame,text="Create",variable=self.v,value="C",indicatoron=0,height=5,width=15, borderwidth=3, command=self.selected)
		self.R1.place(relx=0.4,rely=0.4,anchor=CENTER)
		
		self.R2 = Radiobutton(frame,text="Update",variable=self.v,value="U",indicatoron=0,height=5,width=15, borderwidth=3, command=self.selected)
		self.R2.place(relx=0.4,rely=0.6,anchor=CENTER)

		#Button quit
        	self.button = Button(
            		frame, text="QUIT", fg="white", bg="black", command=frame.quit
           	)
        	self.button.place(relx=0.83,rely=0.80,anchor=W);

	def selected(self):
		v = self.v.get()
		if v=="C":
			self.createAction()
		if v=="U":
			self.updateAction()

	def createAction(self):
		print "Create"
		
		#Open new window
		ctop = Toplevel()
		ctop.title("Import/Export: Create")
		ctop.geometry("300x300")

		#Frame
		cframe = Frame(ctop,height=270,width=250)
		cframe.place(relx=0.01,rely=0.01)

		clframe = Label(cframe,text="Insert CKAN open data url:",font=("Helvetica", 14))
		clframe.place(relx=0.5,rely=0.05,anchor=CENTER)

		f = tkFont.Font(clframe, clframe.cget("font"))
		f.configure(underline = True)
		clframe.configure(font=f)

		#Entry test from
		cfromlabel = Label(cframe,text="From: ")
		cfromlabel.place(relx=0.25,rely=0.24,anchor=E)
		
		cfrom = Entry(cframe)
		cfrom.place(relx=0.55,rely=0.34,anchor=CENTER)
		cfrom.delete(0, END)
		cfrom.insert(0, 'http://dati.trentino.it')
		cfrom.focus_set()
	
		#Entry test to
		ctolabel = Label(cframe,text="To: ")
		ctolabel.place(relx=0.2,rely=0.44,anchor=E)

		cto = Entry(cframe)
		cto.place(relx=0.55,rely=0.54,anchor=CENTER)
		cto.delete(0, END)
		cto.insert(0,'https://data.lab.fi-ware.org')
		cto.focus_set()

		#Entry test authCode
		authlabel = Label(cframe,text="User's Authentication Code: ")
		authlabel.place(relx=0.08,rely=0.64,anchor=W)

		auth = Entry(cframe)
		auth.place(relx=0.55,rely=0.74,anchor=CENTER)
		auth.focus_set()

		#Button create action
		def createButtonAction():
			f = cfrom.get()
			t = cto.get()
			c = auth.get()
			print "Values of Entries"
			print f
			print t
			print c
			createPackageGroup.setGlobalVariable(f,t,c)
			createPackageGroup.main()
		
		#Button create
		cbutton = Button(ctop, text="Create",bg="ivory2", command=createButtonAction)
		cbutton.place(relx=0.70,rely=0.95,anchor=SW);

		#Button back
		cbutton = Button(ctop, text="Back",bg="gray", command=ctop.destroy)
		cbutton.place(relx=0.25,rely=0.95,anchor=SE);
		

	def updateAction(self):
		print "Update"

		#Open new window
		utop = Toplevel()
		utop.title("Import/Export: Update")
		utop.geometry("300x300")

		#Frame
		uframe = Frame(utop,height=270,width=250)
		uframe.place(relx=0.01,rely=0.01)

		ulframe = Label(uframe,text="Insert CKAN open data url:",font=("Helvetica", 14))
		ulframe.place(relx=0.5,rely=0.05,anchor=CENTER)
        
		f = tkFont.Font(ulframe, ulframe.cget("font"))
		f.configure(underline = True)
		ulframe.configure(font=f)

		#Entry test from
		ufromlabel = Label(uframe,text="From: ")
		ufromlabel.place(relx=0.25,rely=0.24,anchor=E)
		
		ufrom = Entry(uframe)
		ufrom.place(relx=0.55,rely=0.34,anchor=CENTER)
		ufrom.delete(0, END)
		ufrom.insert(0, 'http://dati.trentino.it')
		ufrom.focus_set()
	
		#Entry test to
		utolabel = Label(uframe,text="To: ")
		utolabel.place(relx=0.2,rely=0.44,anchor=E)

		uto = Entry(uframe)
		uto.place(relx=0.55,rely=0.54,anchor=CENTER)
		uto.delete(0, END)
		uto.insert(0, 'https://data.lab.fi-ware.org')
		uto.focus_set()

		#Entry test authCode
		authlabel = Label(uframe,text="User's Authentication Code: ")
		authlabel.place(relx=0.08,rely=0.64,anchor=W)

		auth = Entry(uframe)
		auth.place(relx=0.55,rely=0.74,anchor=CENTER)
		auth.focus_set()

		#Button update action
		def updateButtonAction():
			f = ufrom.get()
			t = uto.get()
			c = auth.get()
			print "Values of Entries"
			print f
			print t
			print c
			updatePackageGroup.setGlobalVariable(f,t,c)
			updatePackageGroup.main()
		
		#Button create
		ubutton = Button(utop, text="Update",bg="ivory2", command=updateButtonAction)
		ubutton.place(relx=0.70,rely=0.95,anchor=SW);

		#Button back
		ubutton = Button(utop, text="Back",bg="gray", command=utop.destroy)
		ubutton.place(relx=0.25,rely=0.95,anchor=SE);

def aboutInfo():
	top = Toplevel()
	top.title("About this application..")

	about_message = "Copyright 2012-2013 Trento RISE.\n Licensed under the Apache License, Version 2.0 (the 'License');\n you may not use this file except in compliance with the License.\n You may obtain a copy of the License at\n http://www.apache.org/licenses/LICENSE-2.0\n Unless required by applicable law or agreed to in writing, software\n distributed under the License is distributed on an 'AS IS' BASIS,\n WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n See the License for the specific language governing permissions and\n limitations under the License."
	
	msg = Message(top, text=about_message)
	msg.pack()

	button = Button(top, text="Dismiss", command=top.destroy)
	button.pack()

def notImplemented():
	print "Ops.. Not yet implemented!"


root = Tk()
root.geometry("460x400")
root.wm_title("CKAN/CKAN Harvester")

#Create a top level menu
menubar = Menu(root)

# create a pulldown menu, and add it to the menu bar
filemenu = Menu(menubar, tearoff=0)
filemenu.add_command(label="Open",command=notImplemented)
filemenu.add_command(label="Save",command=notImplemented)
filemenu.add_separator()
filemenu.add_command(label="Exit", command=root.quit)
menubar.add_cascade(label="File", menu=filemenu)

helpmenu = Menu(menubar, tearoff=0)
helpmenu.add_command(label="About", command=aboutInfo)
menubar.add_cascade(label="Help", menu=helpmenu)

#Show menubar
root.config(menu=menubar)

app = App(root)

root.mainloop()
root.destroy() # optional; see description below
