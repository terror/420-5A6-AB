export EDITOR := 'nvim'

default:
	just --list

open path:
	open -a /Applications/Android\ Studio.app {{ path }}
